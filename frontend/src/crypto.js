function toBase64Url(arrayBuffer) {
  
  const bytes = new Uint8Array(arrayBuffer);
  let binary = "";
  for (let i = 0; i < bytes.byteLength; i++) {
    binary += String.fromCharCode(bytes[i]);
  }
  return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
}

function fromBase64Url(base64url) {
  let base64 = base64url.replace(/-/g, '+').replace(/_/g, '/');
  while (base64.length % 4) {
    base64 += '=';
  }
  const binary = atob(base64);
  const bytes = new Uint8Array(binary.length);
  for (let i = 0; i < binary.length; i++) {
    bytes[i] = binary.charCodeAt(i);
  }
  return bytes;
}

export async function chiffrer(texteClair) {
  try {

    const cle = await crypto.subtle.generateKey(
      { name: "AES-GCM", length: 256 },
      true,
      ["encrypt", "decrypt"]
    );

    const iv = crypto.getRandomValues(new Uint8Array(12));
    const donnees = new TextEncoder().encode(texteClair);

    const chiffreBuffer = await crypto.subtle.encrypt(
      { name: "AES-GCM", iv: iv },
      cle,
      donnees
    );

    const cleBrute = await crypto.subtle.exportKey("raw", cle);


    const ivBase64 = toBase64Url(iv);
    const chiffreBase64 = toBase64Url(chiffreBuffer);

    return {
      payload: `${ivBase64}.${chiffreBase64}`,
      cle: toBase64Url(cleBrute)
    };

  } catch (error) {

    console.error("Erreur lors du chiffrement :", error);
    throw error;
  }
}

export async function dechiffrer(payload, cleTexte) {
  try {

    const [ivBase64, chiffreBase64] = payload.split('.');

    const iv = fromBase64Url(ivBase64);
    const ciphertext = fromBase64Url(chiffreBase64);
    const cleBrute = fromBase64Url(cleTexte);


    const cle = await crypto.subtle.importKey(
      "raw",
      cleBrute,
      "AES-GCM",
      true, 
      ["decrypt"]
    );

    const resultatBuffer = await crypto.subtle.decrypt(
      { name: "AES-GCM", iv: iv },
      cle,
      ciphertext
    );

    return new TextDecoder().decode(resultatBuffer);

  } catch (error) {
    console.error("Erreur lors du déchiffrement :", error);
    throw error;
  }
}       