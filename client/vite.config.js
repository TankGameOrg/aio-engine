import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
    publicPath: 'assets',
    plugins: [
        react()
    ],
    server: {
        proxy: {
            '/api': 'http://localhost:4343'
        }
    }
})