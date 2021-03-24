db.auth('admin', 'pa55w0rd')
db = db.getSiblingDB('restaurant')
db.createUser({
    user: 'reskilluser', 
    pwd: 'r3sk1llus3r', 
    roles: [
        { role: 'dbOwner', 
        db: 'restaurant' }
    ] 
})