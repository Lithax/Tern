# 🔒 Tern - Lightweight CLI Encryption & Compression Tool  

**Tern** is a powerful, lightweight command-line tool designed for seamless file encryption, decryption, compression, and decompression. It provides a versatile set of features, including advanced "monofile" operations for combining, encrypting, and verifying files.  

## 🎉 Features  

- **Encrypt/Decrypt Files**: Secure your files with customizable encryption keys.  
- **Compress/Decompress Files**: Save storage space while preserving file integrity.  
- **Monofile Operations**: Combine, compress, and encrypt multiple files into a single mono file and easily reverse the process.  
- **Direct Processing (`-nocopy`)**: Avoid creating additional copies while processing files.  
- **Hash Verification**: Ensure the integrity of your files in a monofile.  
- Lightweight, fast, and easy-to-use interface.  

---

## 🛠️ Installation  

### Clone the repository:  
```bash
git clone https://github.com/Lithax/tern.git
cd tern
```
### Download Distro:
basic.zip -> Needs java 17 or higher locally installed to run

jre_included.zip -> has a JRE included, but is large in file size (Adoptium Java SE 23 minimal JRE)

Use the env.bat file to add Tern to your systemenviroment variables

Build the executable (instructions in the readme of the distros).  

---

## 🖥️ Usage  

The general syntax for Tern commands is:  
```bash
tern [options] [arguments]
```  

### 🔧 Options  

| Option               | Description                                                                 |
|-----------------------|-----------------------------------------------------------------------------|
| `--help`             | Displays the help message with all commands.                               |
| `--version`          | Displays the current version and author.                                   |
| `--enc [key] [files]`| Encrypts files using the specified key. Use `-nocopy` to process directly.  |
| `--denc [key] [files]`| Decrypts files using the specified key. Use `-nocopy` to process directly. |
| `--com [files]`      | Compresses files. Use `-nocopy` to process directly.                       |
| `--dcom [files]`     | Decompresses files. Use `-nocopy` to process directly.                     |
| `--mono [arg] [key] [files]` | Monofile operations (encrypt, decrypt, verify). See below for details. |

### ⚙️ Monofile-Specific Arguments  

| Argument  | Description                                                                 |
|-----------|-----------------------------------------------------------------------------|
| `-in`     | Encrypts and compresses files into a single mono file.                      |
| `-out`    | Decrypts and decompresses a mono file, restoring original files.            |
| `-verify` | Verifies the integrity and hashes of files in a mono file.                  |

---

## 📝 Examples  

### 🔒 Encrypt Files  
Encrypt a file using a key:  
```bash
tern --enc mykey file1.txt
```  

### 🔓 Decrypt Files  
Decrypt an encrypted file using a key:  
```bash
tern --denc mykey file1.enc
```  

### 📥 Compress Files  
Compress a file:  
```bash
tern --com file1.txt
```  

### 📤 Decompress Files  
Decompress a compressed file:  
```bash
tern --dcom file1.com
```  

### 🧩 Monofile Operations  

1. **Create a Mono File**  
   Combine, compress, and encrypt multiple files into a mono file:  
   ```bash
   tern --mono -in mykey file1.txt file2.txt file3.txt
   ```  

   Copy & Paste:  
   ```bash
   tern --mono -in mykey file1.txt file2.txt file3.txt
   ```  

2. **Extract Files from Mono**  
   Decrypt and decompress files from a mono file:  
   ```bash
   tern --mono -out mykey mono_file.tern
   ```  

3. **Verify Mono File Integrity**  
   Verify the hashes of files stored in a mono file:  
   ```bash
   tern --mono -verify mykey mono_file.tern
   ```  

---

## 📖 Documentation  

Detailed documentation is available in the [Wiki](https://github.com/Lithax/tern/wiki).  

---

## 📋 License  

This project is licensed under the Apache 2.0 License. See the [LICENSE](LICENSE) file for more details.  

---

## 🚀 Contributing  

Contributions are welcome! To get started:  

1. Fork the repository.  
2. Create a new branch (`git checkout -b feature-branch`).  
3. Commit your changes (`git commit -m "Add feature"`).  
4. Push to the branch (`git push origin feature-branch`).  
5. Create a Pull Request.  

---

## 🖤 Acknowledgments  

Created with care by **@Lithax**. Visit [GitHub Profile](https://github.com/Lithax).  

---

## 🎨 ASCII Art  

```  
████████╗███████╗██████╗ ███╗   ██╗  
╚══██╔══╝██╔════╝██╔══██╗████╗  ██║  
   ██║   █████╗  ██████╔╝██╔██╗ ██║  
   ██║   ██╔══╝  ██╔══██╗██║╚██╗██║  
   ██║   ███████╗██║  ██║██║ ╚████║  
   ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝  
```  

---

