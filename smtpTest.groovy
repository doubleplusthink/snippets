#!/usr/bin/env groovy

import javax.mail.Session
import javax.mail.Transport
import javax.mail.MessagingException

// ======================================================
// Configuration
// ======================================================

def host = "smtp.example.com"
def port = 587

// Leave blank if authentication is not required
def username = ""
def password = ""

// Encryption
def useSTARTTLS = true
def useSSL = false

// Connection timeout (milliseconds)
def timeout = 10000

// ======================================================

boolean useAuth = username?.trim()

Properties props = new Properties()

props.put("mail.smtp.host", host)
props.put("mail.smtp.port", port.toString())
props.put("mail.smtp.auth", useAuth.toString())
props.put("mail.smtp.starttls.enable", useSTARTTLS.toString())
props.put("mail.smtp.ssl.enable", useSSL.toString())

props.put("mail.smtp.connectiontimeout", timeout.toString())
props.put("mail.smtp.timeout", timeout.toString())

// Print SMTP conversation
props.put("mail.debug", "true")

Session session = Session.getInstance(props)

Transport transport = null

try {
    logger.debug "========================================"
    logger.debug "SMTP Connection Test"
    logger.debug "Host      : $host"
    logger.debug "Port      : $port"
    logger.debug "STARTTLS  : $useSTARTTLS"
    logger.debug "SSL       : $useSSL"
    logger.debug "Auth      : $useAuth"
    logger.debug "========================================"
    logger.debug ""

    transport = session.getTransport("smtp")

    if (useAuth) {
        logger.debug "Connecting with authentication..."
        transport.connect(host, port, username, password)
    } else {
        logger.debug "Connecting without authentication..."
        transport.connect()
    }

    logger.debug ""
    logger.debug "SUCCESS!"
    logger.debug "Successfully connected to SMTP server."

} catch (MessagingException e) {

    logger.debug ""
    logger.debug "FAILED!"
    logger.debug "Exception: ${e.class.name}"
    logger.debug "Message  : ${e.message}"

    logger.debug ""
    logger.debug "Exception chain:"
    Throwable t = e
    while (t != null) {
        logger.debug "----------------------------------------"
        logger.debug t.class.name
        logger.debug t.message
        t = t.cause
    }

    logger.debug ""
    logger.debug "Stack trace:"
    e.printStackTrace(System.out)

} catch (Exception e) {

    logger.debug ""
    logger.debug "Unexpected exception:"
    e.printStackTrace(System.out)

} finally {

    if (transport?.isConnected()) {
        transport.close()
        logger.debug ""
        logger.debug "Connection closed."
    }
}
