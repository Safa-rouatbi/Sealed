import PageCreer from "./PageCreer.jsx";
import PageLire from "./PageLire.jsx";

function App() {
  // /abc123 -> page "Lire" pour le secret abc123, / -> page "Créer"
  const id = window.location.pathname.replace(/\//g, "");

  if (id) {
    return <PageLire id={id} />;
  }
  return <PageCreer />;
}

export default App;
