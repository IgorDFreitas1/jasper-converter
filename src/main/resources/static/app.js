const button = document.getElementById("convertBtn");
const fileInput = document.getElementById("file");
const progress = document.getElementById("progress");
const status = document.getElementById("status");

button.onclick = () => {
    const file = fileInput.files[0];

    if (!file) {
        alert("Selecione um arquivo .jasper");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    progress.style.width = "0%";
    status.innerText = "Convertendo...";

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/api/converter");

    xhr.responseType = "blob";

    xhr.upload.onprogress = (e) => {
        if (e.lengthComputable) {
            const percent = (e.loaded / e.total) * 100;
            progress.style.width = percent + "%";
        }
    };

    xhr.onload = () => {
        if (xhr.status === 200) {
            progress.style.width = "100%";
            status.innerText = "Conversão concluída";

            const blob = xhr.response;
            const link = document.createElement("a");
            link.href = URL.createObjectURL(blob);
            link.download = "convertido.jrxml";
            link.click();
        } else {
            status.innerText = "Erro na conversão";
        }
    };

    xhr.onerror = () => {
        status.innerText = "Erro de conexão";
    };

    xhr.send(formData);
};
