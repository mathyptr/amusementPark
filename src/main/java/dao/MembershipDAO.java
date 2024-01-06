package dao;

import domainModel.membership.Membership;

public interface MembershipDAO {

    /**
     * Returns the membership of the customer with the given fiscal code
     */
    Membership getOfCustomer(String fiscalCode) throws Exception;

    /**
     * Adds a membership for the given customer
      */
    void insertOfCustomer(String fiscalCode, Membership membership) throws Exception;

    /**
     * Updates the membership of the customer with the given fiscal code
    */
    void updateOfCustomer(String fiscalCode, Membership membership) throws Exception;

    /**
     * Removes the membership of the customer with the given fiscal code
     */
    boolean deleteOfCustomer(String fiscalCode) throws Exception;
}
