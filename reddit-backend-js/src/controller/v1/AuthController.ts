import express, { Router } from 'express'

const router = express.Router();

router.post("/signup", async (req, res) => {
    console.log(req.body);
    res.status(200).send("All good!");
})

router.post("/login", async (req, res) => {
    console.log(req.body);
    res.status(200).send("All good!");
})

router.post("/logout", async (req, res) => {
    console.log(req.body);
    res.status(200).send("All good!");
})

router.get("/verification/:token", async (req, res) => {
    console.log(req.body);
    res.status(200).send("All good!");
})

export default router;