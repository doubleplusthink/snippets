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
    println "========================================"
    println "SMTP Connection Test"
    println "Host      : $host"
    println "Port      : $port"
    println "STARTTLS  : $useSTARTTLS"
    println "SSL       : $useSSL"
    println "Auth      : $useAuth"
    println "========================================"
    println ""

    transport = session.getTransport("smtp")

    if (useAuth) {
        println "Connecting with authentication..."
        transport.connect(host, port, username, password)
    } else {
        println "Connecting without authentication..."
        transport.connect()
    }

    println ""
    println "SUCCESS!"
    println "Successfully connected to SMTP server."

} catch (MessagingException e) {

    println ""
    println "FAILED!"
    println "Exception: ${e.class.name}"
    println "Message  : ${e.message}"

    println ""
    println "Exception chain:"
    Throwable t = e
    while (t != null) {
        println "----------------------------------------"
        println t.class.name
        println t.message
        t = t.cause
    }

    println ""
    println "Stack trace:"
    e.printStackTrace(System.out)

} catch (Exception e) {

    println ""
    println "Unexpected exception:"
    e.printStackTrace(System.out)

} finally {

    if (transport?.isConnected()) {
        transport.close()
        println ""
        println "Connection closed."
    }
}
