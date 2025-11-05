const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");

const app = express();
app.use(cors());
app.use(bodyParser.json());

// SOLO guardaremos los datos en memoria
const users = [];

app.post("/users", (req, res) => {
  const { name, email } = req.body;

  if (!name || !email) {
    return res.status(400).json({ message: "Name y Email son obligatorios" });
  }

  users.push({ name, email });

  // 🔥 LOG para verificar que sí está llegando el dato
  console.log("✅ Nuevo usuario recibido:", { name, email });

  res.status(200).json({
    message: "Usuario agregado correctamente",
    users
  });
});

app.listen(3000, () => {
  console.log("🚀 Servidor corriendo en http://localhost:3000");
});
