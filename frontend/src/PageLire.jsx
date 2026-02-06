import { useState } from "react";
import { dechiffrer } from "./crypto.js";
import { lireSecret } from "./api.js";

function messageLecture(erreur) {
  if (erreur.status === 404) {
    return "Ce secret n'existe pas, a expiré ou a déjà été lu.";
  }
  if (erreur.status === 429) {
    return "Trop de requêtes, réessaie dans une minute.";
  }
  return "Impossible de lire le secret.";
}

function PageLire({ id }) {
  const cle = window.location.hash.slice(1);
  const [texte, setTexte] = useState("");
  const [erreur, setErreur] = useState("");
  async function afficher() {
    setErreur("");

    let payload;
    try {
      payload = await lireSecret(id);
    } catch (e) {
      setErreur(messageLecture(e));
      return;
    }

    try {
      setTexte(await dechiffrer(payload, cle));
    } catch {
      setErreur("Impossible de déchiffrer : la clé du lien est incorrecte. Le secret a été supprimé.");
    }
  }

  if (!cle) {
    return <p>Lien invalide : la clé est manquante.</p>;
  }

  return (
    <div>
      <h1>Sealed</h1>
      {texte ? (
        <div>
          <p>Voici le secret. Il a été supprimé du serveur, copie-le maintenant :</p>
          <pre>{texte}</pre>
        </div>
      ) : (
        <div>
          <p>Ce secret ne peut être lu qu'une seule fois.</p>
          <button onClick={afficher}>Afficher le secret</button>
        </div>
      )}
      {erreur && <p>{erreur}</p>}
    </div>
  );
}

export default PageLire;
