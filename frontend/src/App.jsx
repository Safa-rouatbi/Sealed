import PageCreer from "./PageCreer.jsx";
import PageLire from "./PageLire.jsx";

function App() {
  const id = window.location.pathname.replace(/\//g, "");

  return (
    <>
      <header className="entete">
        <a className="logo" href="/">
          Sealed
        </a>
        <p className="devise">Secrets à usage unique</p>
      </header>
      <main className="page">
        <div className="carte">{id ? <PageLire id={id} /> : <PageCreer />}</div>
      </main>
    </>
  );
}

export default App;
