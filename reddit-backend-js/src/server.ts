import express from 'express'
import path from 'path'
import apiRoutesController from './controller/RoutesController'
import dotenv from 'dotenv'
import { initializeDB } from './config/database.config'

// Configure environment variables
dotenv.config()

const app = express();
const port = process.env.APP_PORT || 5000;

// Configure Middleware
app.use('/static', express.static(path.join(__dirname, 'public')))
app.use(express.json())

// Configure Routes
app.use("/api", apiRoutesController)

// Initialize DB
initializeDB().then((sequelize) => {
    sequelize.import("./model");
});

app.listen(port, () => {
    // tslint:disable-next-line:no-console
    console.log(`Reddit Backend JS is listening on port ${port}`);
})