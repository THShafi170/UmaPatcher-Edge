# Building UmaPatcher-Edge from Source

This guide provides in-depth, step-by-step instructions on setting up your environment, configuring Java toolchains, managing signing keystores, and compiling **UmaPatcher-Edge** successfully on your machine.

---

## 📋 Prerequisites

To successfully compile the application, your build environment must meet the following requirements:

### 1. Java Development Kit (JDK)
UmaPatcher-Edge utilizes **Gradle 8.11.1** and modern Kotlin versions, which require a hybrid Java toolchain environment:
* **Gradle Runner (Gradle Daemon)**: Run on **JDK 17** or **JDK 21**. Running on bleeding-edge JDK versions (like JDK 25+) may cause Gradle daemon startup failures due to unsupported major class versions.
* **Compilation Target**: The Kotlin/JVM compile task targets **JDK 8**.
* **Automatic Discovery**: Gradle will automatically scan standard JVM folders, but we recommend explicitly specifying paths if you run multiple JDK versions.

### 2. Android SDK
You need the Android SDK with:
* **Platforms**: Android API Level **34** (`platforms;android-34`)
* **Build-Tools**: Version **34.0.0** or **35.0.0** (`build-tools;34.0.0` or `build-tools;35.0.0`)
* **Platform-Tools** (`platform-tools`)

---

## 🛠️ Environment Setup

### Step 1: Clone the Repository
Clone the repository recursively to ensure all submodules are downloaded:
```bash
git clone --recursive https://github.com/THShafi170/UmaPatcher-Edge.git
cd UmaPatcher-Edge
```

### Step 2: Configure JDK Toolchains
If you do not have JDK 17 and JDK 8 on your system, or if your system default is Java 25+, download and extract them:

#### 🐧 Linux (Adoptium API CLI Example)
```bash
mkdir -p ~/jvm

# Download and extract JDK 17
curl -L -o ~/jvm/jdk17.tar.gz "https://api.adoptium.net/v3/binary/latest/17/ga/linux/x64/jdk/hotspot/normal/eclipse"
tar -xzf ~/jvm/jdk17.tar.gz -C ~/jvm && rm ~/jvm/jdk17.tar.gz

# Download and extract JDK 8
curl -L -o ~/jvm/jdk8.tar.gz "https://api.adoptium.net/v3/binary/latest/8/ga/linux/x64/jdk/hotspot/normal/eclipse"
tar -xzf ~/jvm/jdk8.tar.gz -C ~/jvm && rm ~/jvm/jdk8.tar.gz
```

#### 🪟 Windows (Eclipse Temurin Installers)
1. Download and run the **JDK 17** installer from [Adoptium Eclipse Temurin](https://adoptium.net/temurin/releases/?version=17).
2. Download and run the **JDK 8** installer from [Adoptium Eclipse Temurin](https://adoptium.net/temurin/releases/?version=8).
3. The installers will default to installing under `C:\Program Files\Eclipse Adoptium\`.

#### Configuring gradle.properties (Globally - Highly Recommended)
To keep your local git repository clean and prevent machine-specific directories from being committed to version control, it is highly recommended to configure your JDK toolchain paths inside your **global Gradle configuration** rather than the project-level file.

Create or open the global `gradle.properties` file in your user home directory:
* **🐧 Linux**: `~/.gradle/gradle.properties`
* **🪟 Windows**: `%USERPROFILE%\.gradle\gradle.properties` (e.g., `C:\Users\<username>\.gradle\gradle.properties`)

Append the paths to both of your extracted JDKs:

* **For Linux (`~/.gradle/gradle.properties`)**:
  ```properties
  org.gradle.java.installations.paths=/home/<username>/jvm/jdk-17.0.19+10,/home/<username>/jvm/jdk8u492-b09
  ```
* **For Windows (`%USERPROFILE%\.gradle\gradle.properties` - note the escaped double backslashes)**:
  ```properties
  org.gradle.java.installations.paths=C:\\Program Files\\Eclipse Adoptium\\jdk-17.0.10.7-hotspot,C:\\Program Files\\Eclipse Adoptium\\jdk-8.0.402.7-hotspot
  ```
*(Replace directory names with the actual exact folder names of your JDK installations)*

### Step 3: Configure Android SDK
Create or open `local.properties` in the root of the project and specify your Android SDK location:

* **For Linux**:
  ```ini
  sdk.dir=/home/<username>/Android/Sdk
  ```
* **For Windows (note the escaped backslashes and colon)**:
  ```ini
  sdk.dir=C\:\\Users\\<username>\\AppData\\Local\\Android\\Sdk
  ```

If you do not have the Android SDK or Platforms set up, download the [Android Command Line Tools](https://developer.android.com/studio#command-line-tools-only), extract them, and install the required platform packages:

* **Linux**:
  ```bash
  yes | sdkmanager --sdk_root=/home/<username>/Android/Sdk "platforms;android-34" "build-tools;34.0.0" "platform-tools"
  ```
* **Windows (PowerShell)**:
  ```powershell
  # Accept license and install
  cd C:\path\to\cmdline-tools\bin
  cmd.exe /c "echo y | .\sdkmanager.bat --sdk_root=C:\Users\<username>\AppData\Local\Android\Sdk platforms;android-34 build-tools;34.0.0 platform-tools"
  ```

---

## 🔑 Signing Configurations

By default, the `build.gradle` is configured to sign release builds using environment variables. If these are not specified, it gracefully falls back to using the standard Android debug keystore (`debug.keystore`) so that builds continue to compile cleanly in a local environment.

To configure your own custom release signing, specify these environment variables before building:
* `SIGNING_KEY_FILE`: Path to your private release `.jks` or `.keystore` file.
* `SIGNING_STORE_PASSWORD`: Keystore password.
* `SIGNING_KEY_ALIAS`: Key alias name.
* `SIGNING_KEY_PASSWORD`: Key password.

---

## 🚀 Compiling the Application

Once your JDK, toolchains, and `local.properties` are configured, compiling is extremely straightforward.

### Using Command Line

#### 🐧 Linux (Terminal)
To compile a debug build, run:
```bash
export JAVA_HOME=/home/<username>/jvm/jdk-17.0.19+10
export PATH=$JAVA_HOME/bin:$PATH

./gradlew assembleDebug
```

#### 🪟 Windows (PowerShell)
To compile a debug build, run:
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.10.7-hotspot"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path

.\gradlew.bat assembleDebug
```

#### 🪟 Windows (Command Prompt - CMD)
To compile a debug build, run:
```cmd
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.10.7-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

gradlew.bat assembleDebug
```

The compiled APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### Using Android Studio
1. Open Android Studio.
2. Select **Open an Existing Project** and browse to the cloned `UmaPatcher-Edge` directory.
3. Android Studio will automatically read `local.properties` and synchronize the project.
4. Set the Gradle JDK in Android Studio settings:
   * Navigate to `Settings` -> `Build, Execution, Deployment` -> `Build Tools` -> `Gradle`.
   * Set **Gradle JDK** to your **JDK 17** installation.
5. Click **Build** -> **Make Project** or run `app` on your emulator/device!

---

## ❓ Troubleshooting

### 1. Unsupported class file major version 69 (or similar)
* **Cause**: Gradle is attempting to execute the build runner daemon on a default system Java version (e.g. Java 25+) that is too new for the Gradle version.
* **Fix**: Force the daemon to start on JDK 17 by setting `JAVA_HOME` explicitly:
  * **Linux**:
    ```bash
    export JAVA_HOME=/path/to/jdk-17
    export PATH=$JAVA_HOME/bin:$PATH
    ./gradlew ...
    ```
  * **Windows (PowerShell)**:
    ```powershell
    $env:JAVA_HOME = "C:\path\to\jdk-17"
    $env:Path = "$env:JAVA_HOME\bin;" + $env:Path
    .\gradlew.bat ...
    ```
  * **Windows (CMD)**:
    ```cmd
    set JAVA_HOME=C:\path\to\jdk-17
    set PATH=%JAVA_HOME%\bin;%PATH%
    gradlew.bat ...
    ```

### 2. Cannot find a Java installation matching languageVersion 8
* **Cause**: Kotlin requires compiling using a JDK 8 toolchain, but Gradle could not locate a valid JDK 8 on your machine.
* **Fix**: Ensure you have configured the `org.gradle.java.installations.paths` setting inside your **global `gradle.properties`** (e.g. `~/.gradle/gradle.properties` on Linux or `%USERPROFILE%\.gradle\gradle.properties` on Windows) pointing to your JDK 8 directory as explained in the Environment Setup section above.
