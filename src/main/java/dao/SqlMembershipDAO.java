package dao;

import domainModel.membership.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SqlMembershipDAO implements MembershipDAO {

    private static final String TimeFormat = "dd/MM/yyyy";
    DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern(TimeFormat);
    
    private dbManager db;
    
    public SqlMembershipDAO() {
    	this.db = dbManager.getInstance();
    }
	
	private String membershipTypeToString(Membership membership)
	{
		String type = null;
		
		if(membership instanceof WorkdaysMembershipDecorator)
			type= "workdays";
		else if (membership instanceof SilverMembershipDecorator)
			type= "silver";	
		else if (membership instanceof GoldMembershipDecorator)
			type= "gold";			
		
		return type;		
	}
		
	private Class<? extends Membership> membershipStringToType(Membership membership, String type)
	{
		Class<? extends Membership> mClassConstruct = null;
			if(type.equals("workdays"))
				mClassConstruct= (WorkdaysMembershipDecorator.class);
			else if (type.equals("silver"))
				mClassConstruct=(SilverMembershipDecorator.class);
			else if (type.equals("gold"))
				mClassConstruct=(GoldMembershipDecorator.class);			
		return  mClassConstruct;		
	}	
	
    @Override
    public Membership getOfCustomer(String fiscalCode) throws SQLException {
        Connection connection = db.getConnection();
        Membership membership = null;

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM memberships WHERE customer = ?");
        ps.setString(1, fiscalCode);
        ResultSet rs = ps.executeQuery();               

        if (rs.next()) {
            membership = new EmptyMembership(
            		LocalDate.parse(rs.getString("valid_from"),dataTimeFormat),
            		LocalDate.parse(rs.getString("valid_until"),dataTimeFormat)
            		);
            
            PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM memberships_extensions WHERE customer = ?");
            ps2.setString(1, fiscalCode);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                try {                	
                	Class<? extends Membership> membership1;
                	membership1 =   membershipStringToType(membership,rs2.getString("type"));
                	membership  =	membership1.getConstructor(Membership.class, int.class).newInstance(membership, rs2.getInt("uses"));              	                	
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            rs2.close();
            ps2.close();
        }

        rs.close();
        ps.close();
        db.closeConnection(connection);
        return membership;
    }

	@Override
    public void insertOfCustomer(String fiscalCode, Membership membership) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement insertMembership = connection.prepareStatement("INSERT INTO memberships (customer, valid_from, valid_until) VALUES (?, ?, ?)");
        insertMembership.setString(1, fiscalCode);
        insertMembership.setString(2, dataTimeFormat.format(membership.getValidFrom()));
        insertMembership.setString(3, dataTimeFormat.format(membership.getValidUntil()));
        insertMembership.executeUpdate();
        insertMembership.close();

        insertExtensionsOfCustomer(fiscalCode, membership);

        db.closeConnection(connection);
    }

    @Override
    public void updateOfCustomer(String fiscalCode, Membership membership) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement updateMembership = connection.prepareStatement("UPDATE memberships SET valid_from = ?, valid_until = ? WHERE customer = ?");
        updateMembership.setString(1, dataTimeFormat.format(membership.getValidFrom()));
        updateMembership.setString(2, dataTimeFormat.format(membership.getValidUntil()));
        updateMembership.setString(3, fiscalCode);
        updateMembership.executeUpdate();
        updateMembership.close();

        deleteExtensionsOfCustomer(fiscalCode);
        insertExtensionsOfCustomer(fiscalCode, membership);

        db.closeConnection(connection);
    }

    @Override
    public boolean deleteOfCustomer(String fiscalCode) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement deleteMembership = connection.prepareStatement("DELETE FROM memberships WHERE customer = ?");
        deleteMembership.setString(1, fiscalCode);
        int rows = deleteMembership.executeUpdate();
        deleteMembership.close();

        db.closeConnection(connection);
        return rows > 0;
    }

    private void insertExtensionsOfCustomer(String fiscalCode, Membership membership) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement insertExtension = connection.prepareStatement("INSERT INTO memberships_extensions (customer, type, uses) VALUES (?, ?, ?)");
        while (membership instanceof MembershipDecorator) {
            MembershipDecorator membershipDecorator = (MembershipDecorator) membership;

            insertExtension.setString(1, fiscalCode);
            insertExtension.setString(2, membershipTypeToString(membership));            
            insertExtension.setInt(3, membershipDecorator.getLocalUses());
            insertExtension.executeUpdate();
            membership = membershipDecorator.getMembership();
        }

        insertExtension.close();
        db.closeConnection(connection);
    }

    private void deleteExtensionsOfCustomer(String fiscalCode) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement deleteExtensions = connection.prepareStatement("DELETE FROM memberships_extensions WHERE customer = ?");
        deleteExtensions.setString(1, fiscalCode);
        deleteExtensions.executeUpdate();
        deleteExtensions.close();
        db.closeConnection(connection);
    }
}
