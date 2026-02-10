import { useState } from "react";
import { dechiffrer } from "./crypto.js";
import { lireSecret } from "./api.js";
import { messageErreur } from "./erreurs.js";

function PageLire({ id }) {
  const cle = window.location.hash.slice(1);
  const [texte, setTexte] = useState("");
  const [erreur, setErreur] = useState("");
  const [enCours, setEnCours] = useState(false);

  async function afficher() {
    setErreur("");
    setEnCours(true);

    let payload;
    try {
      payload = await lireSecret(id);
    } catch (e) {
      setErreur(messageErreur(e));
      setEnCours(false);
      return;
    }

    try {
      setTexte(await dechiffrer(payload, cle));
    } catch {
      setErreur("Impossible de déchiffrer : la clé du lien est incorrecte. Le secret a été supprimé.");
    }
    setEnCours(false);
  }

  if (!cle) {
    return (
      <p className="erreur" role="alert">
        Lien invalide : la clé est manquante.
      </p>
    );
  }

  return (
    <section>
      {texte ? (
        <div>
          <p className="intro">Voici le secret. Il a été supprimé du serveur : copie-le maintenant.</p>
          <pre className="secret">{texte}</pre>
        </div>
      ) : (
        <div>
          <p className="intro">
            Ce secret ne peut être lu qu'une seule fois. Une fois affiché, il est supprimé du serveur.
          </p>
          <button className="principal" onClick={afficher} disabled={enCours}>
            Afficher le secret
          </button>
        </div>
      )}

      {erreur && (
        <p className="erreur" role="alert">
          {erreur}
        </p>
      )}
    </section>
  );
}

export default PageLire;
