function erreurAvecStatus(message, status) {
  const erreur = new Error(message);
  erreur.status = status;
  return erreur;
}

async function appeler(url, options) {
  let reponse;
  try {
    reponse = await fetch(url, options);
  } catch {
    throw erreurAvecStatus("Serveur injoignable", 0);
  }

  if (!reponse.ok) {
    throw erreurAvecStatus("Erreur HTTP " + reponse.status, reponse.status);
  }
  return reponse.json();
}

export async function creerSecret(payload, ttlSeconds) {
  const parts = payload.split(".");

  const data = await appeler("/api/secrets", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      ciphertext: parts[1],
      iv: parts[0],
      ttlSeconds: ttlSeconds,
    }),
  });
  return data.id;
}

export async function lireSecret(id) {
  const data = await appeler("/api/secrets/" + encodeURIComponent(id));
  return data.iv + "." + data.ciphertext;
}
