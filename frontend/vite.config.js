import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    // en développement, /api est envoyé au backend Spring Boot
    proxy: {
      "/api": "http://localhost:8080",
    },
  },
});
