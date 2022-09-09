import { Sequelize } from 'sequelize'

let sequelize: Sequelize;

export const initializeDB = async (): Promise<Sequelize> => {

    if (sequelize) {
        return sequelize;
    }

    sequelize = new Sequelize(process.env.DB_SCHEMA, process.env.DB_USERNAME, process.env.DB_PASSWORD, {
        host: 'localhost',
        dialect: 'mysql'
    });

    await sequelize.authenticate().then(() => {
        console.log("DB Connected");
    });

    return sequelize;
}

export const getDBConnection = async (): Promise<Sequelize> => {
    if (sequelize) {
        return sequelize;
    }

    return initializeDB();
}