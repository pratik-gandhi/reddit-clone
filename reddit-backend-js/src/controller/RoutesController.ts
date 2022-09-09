import express, { Router } from 'express'
import routeControllerV1 from './v1/RoutesControllerV1'

const router = express.Router();

router.use("/v1", routeControllerV1)

export default router