https://www.techgalery.com/2021/09/upload-and-download-rest-api-using.html


if (downloadedFile.type !== "") {
        const contentDispositionHeader = event.headers.get('Content-Disposition');
        const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
        const matches = fileNameRegex.exec(contentDispositionHeader);
        const fileName = matches && matches[1] ? matches[1].replace(/['"]/g, '') : 'downloaded_file';
        saveAs(downloadedFile, fileName);
      }
