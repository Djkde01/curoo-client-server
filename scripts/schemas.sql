-- Table: Users
CREATE TABLE Users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    mobile_phone VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trigger function to update modification_date
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.modification_date = NOW();
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply trigger to Users
CREATE TRIGGER set_modification_date_users
BEFORE UPDATE ON Users
FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- Table: Clients
CREATE TABLE Clients (
    client_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES Users(user_id),
    id_type VARCHAR(50) NOT NULL,
    id_number VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Apply trigger to Clients
CREATE TRIGGER set_modification_date_clients
BEFORE UPDATE ON Clients
FOR EACH ROW
EXECUTE FUNCTION update_modified_column();
