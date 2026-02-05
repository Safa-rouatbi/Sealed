export async function creerSecret(payload, ttlSeconds) {
  const parts = payload.split(".");

  const reponse = await fetch("/api/secrets", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      ciphertext: parts[1],
      iv: parts[0],
      ttlSeconds: ttlSeconds,
    }),
  });

  if (!reponse.ok) {
    const erreur = new Error("Erreur HTTP " + reponse.status);
    erreur.status = reponse.status;
    throw erreur;
  }
  

  const data = await reponse.json();
  return data.id;
}

export async function lireSecret(id) {
  const reponse = await fetch("/api/secrets/" + encodeURIComponent(id));

  if (!reponse.ok) {
    const erreur = new Error("Erreur HTTP " + reponse.status);
    erreur.status = reponse.status;
    throw erreur;
  }

  const data = await reponse.json();
  return data.iv + "." + data.ciphertext;
}