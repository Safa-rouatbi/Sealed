import { useState } from "react";
import { chiffrer } from "./crypto.js";
import { creerSecret } from "./api.js";
import { messageErreur } from "./erreurs.js";

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
  const [enCours, setEnCours] = useState(false);

  async function creer() {
    const probleme = verifierTexte(texte);
    setErreur(probleme);
    setCopie(false);
    setLien("");
    if (probleme) {
      return;
    }

    setEnCours(true);
    try {
      const { payload, cle } = await chiffrer(texte);
      const id = await creerSecret(payload, secondes);
      setLien(`${window.location.origin}/${id}#${cle}`);
      setTexte("");
    } catch (e) {
      setErreur(messageErreur(e));
    }
    setEnCours(false);
  }

  async function copier() {
    try {
      await navigator.clipboard.writeText(lien);
      setCopie(true);
    } catch {
      setErreur("Copie impossible. Sélectionne le lien à la main.");
    }
  }

  return (
    <section>
      <p className="intro">
        Écris un secret. Il est chiffré dans ton navigateur et ne peut être lu qu'une seule fois.
      </p>

      <label htmlFor="texte">Ton secret</label>
      <textarea id="texte" value={texte} onChange={(e) => setTexte(e.target.value)} />

      <div className="ligne">
        <div className="champ">
          <label htmlFor="duree">Expire après</label>
          <select id="duree" value={secondes} onChange={(e) => setSecondes(Number(e.target.value))}>
            {DUREES.map((duree) => (
              <option key={duree.secondes} value={duree.secondes}>
                {duree.label}
              </option>
            ))}
          </select>
        </div>
        <button className="principal" onClick={creer} disabled={enCours}>
          Créer le lien
        </button>
      </div>

      {erreur && (
        <p className="erreur" role="alert">
          {erreur}
        </p>
      )}

      {lien && (
        <div className="resultat">
          <label htmlFor="lien">Lien à partager</label>
          <div className="copie">
            <input id="lien" readOnly value={lien} onFocus={(e) => e.target.select()} />
            <button className="secondaire" onClick={copier}>
              {copie ? "Copié" : "Copier"}
            </button>
          </div>
          <p className="note">
            Ce lien ne fonctionne qu'une fois. La clé de déchiffrement se trouve après le # : elle n'est jamais
            envoyée au serveur.
          </p>
        </div>
      )}
    </section>
  );
}

export default PageCreer;
