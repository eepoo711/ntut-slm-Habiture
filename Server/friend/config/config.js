module.exports = {
    server: {
        port: process.env.SERVER_PORT || "7101",
        httpsPort: process.env.PORT || "1443"
    },
    db: {
        user: process.env.DB_USER || 'root',
        host: process.env.DB_HOST || '127.0.0.1',
        database: process.env.DB_NAME || 'habiture',
        password: process.env.DB_PWD || '1234567890',
        port: process.env.DB_PORT || 3306
    },
    profileUrl: process.env.PROFILE_URL || 'http://192.168.99.1/profile/'
}