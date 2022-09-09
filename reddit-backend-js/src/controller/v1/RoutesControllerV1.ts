import express from 'express';
import authController from './AuthController'

const router = express.Router();

router.use("/auth", authController)

export default router