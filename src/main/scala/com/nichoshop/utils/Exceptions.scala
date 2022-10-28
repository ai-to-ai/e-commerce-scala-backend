package com.nichoshop.utils

class UserNotFoundException(message: String = "USER_NOT_FOUND") extends Exception(message)

class AuthenticationException(message: String) extends Exception(message)

class NotAuthorizedException(message: String = "NOT_AUTHORIZED") extends Exception(message)

class AuthSessionExpiredOrNotExistsException(message: String) extends Exception(message)

class TwoFactorAuthenticationException(message: String) extends Exception(message)

class TokenIsCompromisedException(message: String = "TOKEN_EXPIRED") extends Exception(message)