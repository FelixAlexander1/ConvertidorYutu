async function convertir() {
    const url = document.getElementById('url').value;
    if (!url) {
        alert('Por favor, introduce una URL.');
        return;
    }

    document.getElementById('mensaje').innerText = 'Convirtiendo... espera por favor...';

    try {
        const response = await fetch('/convert', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ url: url, format: 'mp3' })
        });

        const data = await response.json(); // Siempre intenta leer la respuesta

        if (!response.ok) {
            // Si falla, mostramos el mensaje del backend si existe
            const errorMessage = data?.message || 'Error en la conversión.';
            throw new Error(errorMessage);
        }

        if (data.success) {
            document.getElementById('mensaje').innerText = '¡Conversión exitosa! Descargando...';
            const fileName = data.fileName;
            window.location.href = `/download?file=${encodeURIComponent(fileName)}`;
        } else {
            document.getElementById('mensaje').innerText = data.message || 'Error al convertir el video.';
        }
    } catch (error) {
        console.error(error);
        document.getElementById('mensaje').innerText = `Error: ${error.message}`;
    }
}
