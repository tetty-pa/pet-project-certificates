# Authentication-SpringSecurity-task

Authentication & Spring Security

Practice
Recommended Timeline
The recommended timeline for the whole module is 2 weeks.

Task

This module is an extension of REST API Advanced module and covers following topics:

Spring Security framework
Oauth2 and OpenId Connect
JWT token
Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications. OAuth 2.0 is a security standard where you give one application permission to access your data in another application. The steps to grant permission, or consent, are often referred to as authorization or even delegated authorization. You authorize one application to access your data, or use features in another application on your behalf, without giving them your password. OpenID Connect (OIDC) is a thin layer that sits on top of OAuth 2.0 that adds login and profile information about the person who is logged in. JSON Web Tokens are JSON objects used to send information between parties in a compact and secure manner.

Application requirements
Spring Security should be used as a security framework.
Application should support only stateless user authentication and verify integrity of JWT token.
Users should be stored in a database with some basic information and a password.
User Permissions:

 - Guest:
    * Read operations for main entity.
    * Signup.
    * Login.
 - User:
    * Make an order on main entity.
    * All read operations.
 - Administrator (can be added only via database call):
    * All operations, including addition and modification of entities.
Get acquainted with the concepts Oauth2 and OpenId Connect
(Optional task) Use Oauth2 as an authorization protocol. a. OAuth2 scopes should be used to restrict data. b. Implicit grant and Resource owner credentials grant should be implemented.
(Optional task) It's allowed to use Spring Data. Requirement for this task - all repository (and existing ones) should be migrated to Spring Data.

