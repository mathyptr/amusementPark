package dao;

import domainModel.membership.*;
import db.dbManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.Map;

import static java.util.Map.Entry;

import java.lang.reflect.InvocationTargetException;

public class SqlMembershipDAO implements MembershipDAO {


	
	private String membershipTypeToString(Membership membership)
	{
		String type = null;
	
		return type;		
	}
	
	
	
	private Membership membershipStringToType(Membership membership, String type)
	{
		//Membership membership = null;

		return membership;		
	}
	
	
    @Override
    public Membership getOfCustomer(String fiscalCode) throws SQLException {
        Connection connection = dbManager.getConnection();
        Membership membership = null;
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM memberships WHERE customer = ?");
        ps.setString(1, fiscalCode);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            membership = new EmptyMembership(LocalDate.parse(rs.getString("valid_from")), LocalDate.parse(rs.getString("valid_until")));

            PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM membership_extensions WHERE customer = ?");
            ps2.setString(1, fiscalCode);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                try {
//Mathy                    membership = membershipStringToType.get(rs2.getString("type")).getConstructor(Membership.class, int.class).newInstance(membership, rs2.getInt("uses"));
                    	membership = membershipStringToType(membership,rs2.getString("type"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            rs2.close();
            ps2.close();
        }

        rs.close();
        ps.close();
        dbManager.closeConnection(connection);
        return membership;
    }

	@Override
    public void insertOfCustomer(String fiscalCode, Membership membership) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement insertMembership = connection.prepareStatement("INSERT INTO memberships (customer, valid_from, valid_until) VALUES (?, ?, ?)");
        insertMembership.setString(1, fiscalCode);
        insertMembership.setString(2, membership.getValidFrom().toString());
        insertMembership.setString(3, membership.getValidUntil().toString());
        insertMembership.executeUpdate();
        insertMembership.close();

        insertExtensionsOfCustomer(fiscalCode, membership);

        dbManager.closeConnection(connection);
    }

    @Override
    public void updateOfCustomer(String fiscalCode, Membership membership) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement updateMembership = connection.prepareStatement("UPDATE memberships SET valid_from = ?, valid_until = ? WHERE customer = ?");
        updateMembership.setString(1, membership.getValidFrom().toString());
        updateMembership.setString(2, membership.getValidUntil().toString());
        updateMembership.setString(3, fiscalCode);
        updateMembership.executeUpdate();
        updateMembership.close();

        deleteExtensionsOfCustomer(fiscalCode);
        insertExtensionsOfCustomer(fiscalCode, membership);

        dbManager.closeConnection(connection);
    }

    @Override
    public boolean deleteOfCustomer(String fiscalCode) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement deleteMembership = connection.prepareStatement("DELETE FROM memberships WHERE customer = ?");
        deleteMembership.setString(1, fiscalCode);
        int rows = deleteMembership.executeUpdate();
        deleteMembership.close();

        dbManager.closeConnection(connection);
        return rows > 0;
    }

    private void insertExtensionsOfCustomer(String fiscalCode, Membership membership) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement insertExtension = connection.prepareStatement("INSERT INTO membership_extensions (customer, type, uses) VALUES (?, ?, ?)");
        while (membership instanceof MembershipDecorator) {
            MembershipDecorator membershipDecorator = (MembershipDecorator) membership;

            insertExtension.setString(1, fiscalCode);
//mathy            insertExtension.setString(2, membershipTypeToString.get(membership.getClass()));
            insertExtension.setString(2, membershipTypeToString(membership));            
            insertExtension.setInt(3, membershipDecorator.getLocalUses());
            insertExtension.executeUpdate();
            membership = membershipDecorator.getMembership();
        }

        insertExtension.close();
        dbManager.closeConnection(connection);
    }

    private void deleteExtensionsOfCustomer(String fiscalCode) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement deleteExtensions = connection.prepareStatement("DELETE FROM membership_extensions WHERE customer = ?");
        deleteExtensions.setString(1, fiscalCode);
        deleteExtensions.executeUpdate();
        deleteExtensions.close();
        dbManager.closeConnection(connection);
    }
}
