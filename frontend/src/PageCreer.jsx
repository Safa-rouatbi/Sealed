import { useState } from "react";
import { chiffrer } from "./crypto.js";
import { creerSecret } from "./api.js";
const MAX_OCTETS = 74000;

const DUREES = [
  { label: "5 minutes", secondes: 300 },
  { label: "1 heure", secondes: 3600 },
  { label: "24 heures", secondes: 86400 },
  { label: "7 jours", secondes: 604800 },
];

function verifierTexte(texte) {
  if (texte.trim() === "") {
    return "Écris un secret avant de continuer.";
  }
  if (new TextEncoder().encode(texte).length > MAX_OCTETS) {
    return "Le texte est trop long.";
  }
  return "";
}

function PageCreer() {
  const [texte, setTexte] = useState("");
  const [secondes, setSecondes] = useState(3600);
  const [lien, setLien] = useState("");
  const [erreur, setErreur] = useState("");
  const [copie, setCopie] = useState(false);

  async function creer() {
    const probleme = verifierTexte(texte);
    setErreur(probleme);
    setCopie(false);
    if (probleme) {
      return;
    }

    try {
      const { payload, cle } = await chiffrer(texte);
      const id = await creerSecret(payload, secondes);
      // la clé est après le # dans l url
      setLien(`${window.location.origin}/${id}#${cle}`);
      setTexte("");
    } catch (e) {
      setErreur(e.status === 429 ? "Trop de requêtes, réessaie dans une minute." : "Impossible de créer le secret.");
    }
  }

  async function copier() {
    await navigator.clipboard.writeText(lien);
    setCopie(true);
  }

  return (
    <div>
      <h1>Sealed</h1>
      <p>Écris un secret : il est chiffré dans ton navigateur et ne peut être lu qu'une seule fois.</p>

      <textarea rows="8" cols="50" value={texte} onChange={(e) => setTexte(e.target.value)} />

      <div>
        <select value={secondes} onChange={(e) => setSecondes(Number(e.target.value))}>
          {DUREES.map((duree) => (
            <option key={duree.secondes} value={duree.secondes}>
              {duree.label}
            </option>
          ))}
        </select>
        <button onClick={creer}>Créer le lien</button>
      </div>

      {erreur && <p>{erreur}</p>}

      {lien && (
        <div>
          <p>Envoie ce lien (il ne fonctionne qu'une fois) :</p>
          <input readOnly size="60" value={lien} />
          <button onClick={copier}>{copie ? "Copié !" : "Copier"}</button>
        </div>
      )}
    </div>
  );
}

export default PageCreer;
