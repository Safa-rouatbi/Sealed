export function messageErreur(erreur) {
  const status = erreur.status;

  if (status === 404) {
    return "Ce secret n'existe pas, a expiré ou a déjà été lu.";
  }
  if (status === 429) {
    return "Trop de requêtes. Réessaie dans une minute.";
  }
  if (status === 400) {
    return "La demande est invalide. Vérifie le texte et la durée.";
  }
  if (status === 0 || status >= 500) {
    return "Le serveur est indisponible. Réessaie plus tard.";
  }
  return "Une erreur est survenue. Réessaie plus tard.";
}
