///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.smsa.highValueAlerts.utils;
//
///**
// *
// * @author abcom
// */
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.stereotype.Service;
//import com.authentication.UPI.entity.UPIUser;
//import com.authentication.UPI.models.AuthRequest;
//import javax.naming.AuthenticationException;
//import javax.naming.CommunicationException;
//import javax.naming.NamingEnumeration;
//import javax.naming.NamingException;
//import javax.naming.directory.*;
//import javax.naming.ldap.InitialLdapContext;
//import javax.naming.ldap.LdapContext;
//import java.util.Hashtable;
//
//@Service
//public class LdapService {
//
//    private static final Logger logger = LogManager.getLogger(LdapService.class);
//    static String lHostName = "10.24.153.129";
//    static String lDomainName = "icicibankltd.com";
//    static String lPort = "389";
//    static String lLDAPAppl = "ldap";
//    static boolean domainStatus = false;
//
//    private static Hashtable<String, String> constructEnvironment(String username, String password, String domainName,
//            String hostName) {
//        Hashtable<String, String> lEnv = new Hashtable<>();
//        logger.info("Constructing LDAP environment for: {}@{}", username, domainName);
//
//        lEnv.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
//        lEnv.put("java.naming.referral", "follow");
//        lEnv.put("java.naming.security.principal", username + "@" + domainName);
//        lEnv.put("java.naming.security.credentials", password);
//        lEnv.put("java.naming.security.authentication", "simple");
//        lEnv.put("java.naming.provider.url", lLDAPAppl + "://" + hostName + ":" + lPort);
//        lEnv.put("com.sun.jndi.ldap.connect.timeout", "20000");
//        lEnv.put("com.sun.jndi.ldap.read.timeout", "20000");
//
//        return lEnv;
//    }
//
//    public UPIUser ldapAuthService(AuthRequest request) throws NamingException {
//
//        Hashtable<String, String> lEnv;
//        try {
//
//            lEnv = constructEnvironment(request.getUserName(), request.getPassword(), lDomainName, lHostName);
//            logger.info("Constructed LDAP environment for user: {}", request.getUserName());
//            LdapContext lLdapContext = new InitialLdapContext(lEnv, null);
//
//            logger.info("Connected to LDAP server: {}", lLdapContext.getEnvironment().get("java.naming.provider.url"));
//
//            boolean isAuthenticated = authenticateLDAPUser(lLdapContext, request.getUserName(), request.getPassword(),
//                    lDomainName);
//
//            if (isAuthenticated) {
//               
//                return findAccountByAccountName(lLdapContext, "DC=icicibankltd,DC=com", request.getUserName());
//            } else {
//                
//                throw new NamingException("User authentication failed: Invalid credentials.");
//
//            }
//
//        } catch (CommunicationException cex) {
//        
//            logger.error("CommunicationException during LDAP auth: {}", cex.getMessage(), cex);
//            throw new NamingException("Unable to connect to the LDAP server.");
//        } catch (AuthenticationException aex) {
//        
//            logger.error("AuthenticationException: {}", aex.getMessage(), aex);
//            throw new NamingException("Invalid credentials.");
//        } catch (Exception ex) {
//            
//            logger.error("Unexpected error in LDAP authentication: {}", ex.getMessage(), ex);
//            throw new NamingException("Unexpected error in LDAP authentication: " + ex.getMessage());
//        }
//
//    }
//
//    public static boolean authenticateLDAPUser(LdapContext ldapContext, String username, String password,
//            String domainName) {
//        try {
//            if (domainName != null && !domainName.trim().isEmpty()) {
//                username = username + "@" + domainName.trim();
//            }
//
//            ldapContext.addToEnvironment("java.naming.security.principal", username);
//            ldapContext.addToEnvironment("java.naming.security.credentials", password);
//            ldapContext.reconnect(null);
//
//            logger.info("Authentication successful for user: {}", username);
//            return true;
//
//        } catch (AuthenticationException aex) {
//            logger.error("Authentication failed: Invalid credentials for user {}", username);
//        } catch (NamingException nex) {
//            logger.error("NamingException during authentication: {}", nex.getMessage(), nex);
//        } catch (Exception ex) {
//            logger.error("Unexpected error during LDAP user authentication: {}", ex.getMessage(), ex);
//        }
//
//        return false;
//    }
//
//    public static UPIUser findAccountByAccountName(DirContext ctx, String ldapSearchBase, String accountName)
//            throws NamingException {
//        UPIUser userDetails = new UPIUser();
//        String userId = null;
//
//        try {
//            String searchFilter = "(&(objectClass=user)(sAMAccountName=" + accountName + "))";
//            SearchControls searchControls = new SearchControls();
//            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//            NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);
//
//            while (results.hasMore()) {
//                SearchResult sr = results.next();
//
//                if (userId == null) {
//                    userId = sr.getNameInNamespace();
//                }
//
//                Attributes attrs = sr.getAttributes();
//                logAttributes(attrs);
//
//                if (attrs != null) {
//                    userDetails.setLoginId(getAttributeValue(attrs, "mailNickname"));
//                    userDetails.setFirstName(getAttributeValue(attrs, "givenName"));
//                    userDetails.setEmail(getAttributeValue(attrs, "mail"));
//                }
//            }
//
//        } catch (Exception e) {
//            logger.error("Exception while finding account by accountName: {}", e.getMessage(), e);
//        }
//
//        logger.info("User LDAP ID resolved as: {}", userId);
//        return userDetails;
//    }
//
//    private static void logAttributes(Attributes attrs) {
//        if (attrs == null) {
//            logger.info("No attributes found.");
//            return;
//        }
//
//        try {
//            NamingEnumeration<? extends Attribute> ae = attrs.getAll();
//            while (ae.hasMore()) {
//                Attribute attr = ae.next();
//                logger.info("Attribute: {}", attr.getID());
//
//                NamingEnumeration<?> values = attr.getAll();
//                while (values.hasMore()) {
//                    logger.info(" - Value: {}", values.next());
//                }
//            }
//        } catch (NamingException e) {
//            logger.error("Error while printing LDAP attributes: {}", e.getMessage(), e);
//        }
//    }
//
//    private static String getAttributeValue(Attributes attrs, String attributeName) {
//        try {
//            Attribute attr = attrs.get(attributeName);
//            if (attr != null) {
//                return (String) attr.get();
//            }
//        } catch (NamingException e) {
//            logger.error("Error getting attribute [{}]: {}", attributeName, e.getMessage(), e);
//        }
//        return null;
//    }
//}