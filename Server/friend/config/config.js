module.exports = {
    server: {
        port: process.env.SERVER_PORT || "7101",
        httpsPort: process.env.PORT || "1443"
    },
    nosqlDb: {
        host: process.env.NOSQL_DB_HOST || 'mongodb://127.0.0.1',
        database: process.env.NOSQL_DB_NAME || 'habiture',
    },
    profileUrl: process.env.PROFILE_URL || 'http://192.168.99.1/profile/'
}