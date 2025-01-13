import { useState } from "react";
import "./App.css";

function App() {
  // State for knowledge upload
  const [knowledgeText, setKnowledgeText] = useState("");

  // State for text generation
  const [prompt, setPrompt] = useState("");
  const [responseText, setResponseText] = useState("");

  // Handler: Upload Knowledge
  const handleKnowledgeUpload = async () => {
    try {
      const formData = new FormData();
      formData.append("text", knowledgeText);

      const res = await fetch("http://localhost:8082/api/v1/rag/knowledge", {
        method: "POST",
        body: formData,
      });

      if (res.ok) {
        alert("Knowledge uploaded successfully!");
        setKnowledgeText("");
      } else {
        alert("Error uploading knowledge.");
      }
    } catch (err) {
      console.error(err);
      alert("An error occurred while uploading knowledge.");
    }
  };

  // Handler: Generate Text
  const handleGenerateText = async () => {
    try {
      const formData = new FormData();
      formData.append("question", prompt);

      const res = await fetch("http://localhost:8082/api/v1/rag/ask-question", {
        method: "POST",
        body: formData,
      });

      if (res.ok) {
        const text = await res.text();
        setResponseText(text);
      } else {
        alert("Error generating text.");
      }
    } catch (err) {
      console.error(err);
      alert("An error occurred while generating text.");
    }
  };

  return (
    <div style={{ margin: "2rem" }}>
      <h1>Retrieval-Augmented Generation (RAG) Demo</h1>

      <section style={{ marginBottom: "2rem" }}>
        <h2>1) Upload Knowledge</h2>
        <br />
        <div>
          <textarea
            rows="5"
            cols="60"
            placeholder="Paste knowledge text here..."
            value={knowledgeText}
            onChange={(e) => setKnowledgeText(e.target.value)}
          />
        </div>
        <button onClick={handleKnowledgeUpload} style={{ marginTop: "1rem" }}>
          Upload Knowledge
        </button>
      </section>

      <section style={{ marginBottom: "2rem" }}>
        <h2>2) Generate Text from Knowledge</h2>
        <p>Ask a question or provide a prompt:</p>
        <textarea
          rows="4"
          cols="60"
          placeholder="Type your prompt/question here..."
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
        />
        <br />
        <button onClick={handleGenerateText} style={{ marginTop: "1rem" }}>
          Generate Text
        </button>
      </section>

      {responseText && (
        <section>
          <h3>Generated Response:</h3>
          <div
            style={{
              border: "1px solid #ccc",
              padding: "1rem",
              width: "80%",
              backgroundColor: "#f9f9f9",
            }}
          >
            {responseText}
          </div>
        </section>
      )}
    </div>
  );
}

export default App;
