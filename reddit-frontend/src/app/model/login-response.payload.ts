export interface LoginResponsePayload {
    username: string,
    jwt: string,
    expiresAt: Date,
    refreshToken: string
}