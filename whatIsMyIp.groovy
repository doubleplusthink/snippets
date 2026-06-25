try {
    def ip = new URL("https://api.ipify.org").text.trim()
    logger.debug "Public IP: ${ip}"
} catch (Exception e) {
    logger.debug  "Unable to determine public IP"
    e.printStackTrace()
}
