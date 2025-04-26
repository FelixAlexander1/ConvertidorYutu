async function convertir() {
    const url = document.getElementById('url').value;
    if (!url) {
        alert('Por favor, introduce una URL.');
        return;
    }

    document.getElementById('mensaje').innerText = 'Convirtiendo... espera por favor...';

    try {
        // 1. Llamamos al backend para convertir
        const response = await fetch('/convert', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ url: url, format: 'mp3' })
        });

        if (!response.ok) {
            throw new Error('Error en la conversión.');
        }

        const data = await response.json();

        if (data.success) {
            document.getElementById('mensaje').innerText = '¡Conversión exitosa! Descargando...';

            // 2. Descargamos el archivo
            const fileName = data.fileName;
            window.location.href = `/download?file=${encodeURIComponent(fileName)}`;
        } else {
            document.getElementById('mensaje').innerText = 'Error al convertir el video.';
        }
    } catch (error) {
        console.error(error);
        document.getElementById('mensaje').innerText = 'Error en el proceso.';
    }
}
