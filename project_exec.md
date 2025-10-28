# Project Execution Plan: Store Keeper App

This document outlines the detailed steps for implementing the Store Keeper App, following the project plan.

## Phase 1: Project Setup and Core Data Model

### 1.1 Project Setup

*   **Verify Android Studio Project:** Ensure the existing project is correctly configured for Kotlin and Jetpack Compose.
*   **Update `gradle/libs.versions.toml`:**
    *   Add versions for Room, Compose BOM, Compose Compiler, Lifecycle, Activity Compose, and Navigation Compose.
    *   Example additions:
        ```toml
        room = "2.6.1"
        compose-bom = "2024.06.00"
        compose-compiler = "1.5.1"
        lifecycle = "2.8.3"
        activity-compose = "1.9.0"
        navigation-compose = "2.7.7"
        ```
    *   Add library entries:
        ```toml
        androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
        androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
        androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
        androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
        androidx-ui = { group = "androidx.compose.ui", name = "ui" }
        androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
        androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
        androidx-material3 = { group = "androidx.compose.material3", name = "material3" }
        androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
        androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activity-compose" }
        androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation-compose" }
        ```
    *   Add `kotlin-kapt` plugin entry:
        ```toml
        kotlin-kapt = { id = "org.jetbrains.kotlin.kapt", version.ref = "kotlin" }
        ```
*   **Update `app/build.gradle.kts`:**
    *   Apply `kotlin-kapt` plugin: `id("org.jetbrains.kotlin.kapt")`
    *   Set `compileSdk` to 35 (or higher if recommended by latest dependencies).
    *   Enable Compose:
        ```kotlin
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        ```
    *   Add Room dependencies:
        ```kotlin
        implementation(libs.androidx.room.runtime)
        kapt(libs.androidx.room.compiler)
        implementation(libs.androidx.room.ktx)
        ```
    *   Add Jetpack Compose dependencies:
        ```kotlin
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.navigation.compose)
        ```
*   **Sync Gradle:** Run `./gradlew assembleDebug` to ensure all dependencies are resolved.

### 1.2 Room Database

*   **Create `data` package:** `com.umarndungotech.storekeeper.data`
*   **Define `User` Entity:**
    *   Create `User.kt` in `data/model` package.
    *   Annotate with `@Entity(tableName = "users")`.
    *   Properties: `id: Int` (primary key, auto-generate), `name: String`, `email: String`, `password: String`.
*   **Define `Product` Entity:**
    *   Create `Product.kt` in `data/model` package.
    *   Annotate with `@Entity(tableName = "products")`.
    *   Properties: `id: Int` (primary key, auto-generate), `name: String`, `quantity: Int`, `price: Double`, `imageUri: String?`.
*   **Create `UserDao` Interface:**
    *   Create `UserDao.kt` in `data/dao` package.
    *   Annotate with `@Dao`.
    *   Methods: `@Insert suspend fun insert(user: User)`, `@Query("SELECT * FROM users WHERE email = :email") suspend fun getUserByEmail(email: String): User?`.
*   **Create `ProductDao` Interface:**
    *   Create `ProductDao.kt` in `data/dao` package.
    *   Annotate with `@Dao`.
    *   Methods: `@Insert suspend fun insert(product: Product)`, `@Update suspend fun update(product: Product)`, `@Delete suspend fun delete(product: Product)`, `@Query("SELECT * FROM products") fun getAllProducts(): Flow<List<Product>>`, `@Query("SELECT * FROM products WHERE id = :id") suspend fun getProductById(id: Int): Product?`.
*   **Create `AppDatabase` Abstract Class:**
    *   Create `AppDatabase.kt` in `data` package.
    *   Annotate with `@Database(entities = [User::class, Product::class], version = 1, exportSchema = false)`.
    *   Extend `RoomDatabase`.
    *   Abstract methods for DAOs: `abstract fun userDao(): UserDao`, `abstract fun productDao(): ProductDao`.
    *   Companion object with `getInstance` method for singleton pattern.

## Phase 2: Authentication

### 2.1 UI - Login and Signup Screens (Jetpack Compose)

*   **Create `ui` package:** `com.umarndungotech.storekeeper.ui`
*   **Create `auth` package:** `com.umarndungotech.storekeeper.ui.auth`
*   **Create `SignupScreen.kt`:**
    *   Composable function `SignupScreen(navController: NavController, authViewModel: AuthViewModel)`.
    *   `Column` layout with `TextField` for Name, Email, Password, Confirm Password.
    *   `Button` for "Sign Up".
    *   `Text` with `ClickableText` for "Already have an account? Login".
    *   Input validation (e.g., email format, password match).
*   **Create `LoginScreen.kt`:**
    *   Composable function `LoginScreen(navController: NavController, authViewModel: AuthViewModel)`.
    *   `Column` layout with `TextField` for Email, Password.
    *   `Button` for "Login".
    *   `Text` with `ClickableText` for "Don't have an account? Sign Up".
    *   Input validation.

### 2.2 ViewModel

*   **Create `AuthViewModel.kt`:**
    *   Create `viewmodel` package: `com.umarndungotech.storekeeper.viewmodel`
    *   Extend `ViewModel`.
    *   Inject `UserDao` (or a repository that wraps it).
    *   `signUp(name, email, password)`: Inserts new user into DB.
    *   `login(email, password)`: Retrieves user by email and verifies password.
    *   `MutableStateFlow` or `LiveData` for UI state (e.g., `_signupSuccess: MutableStateFlow<Boolean>`, `_loginSuccess: MutableStateFlow<Boolean>`, `_errorMessage: MutableStateFlow<String>`).

### 2.3 Navigation

*   **Create `navigation` package:** `com.umarndungotech.storekeeper.navigation`
*   **Create `AppNavHost.kt`:**
    *   Composable function `AppNavHost(navController: NavHostController)`.
    *   Define routes: `AuthScreen.Login.route`, `AuthScreen.Signup.route`, `ProductScreen.Dashboard.route`.
    *   Use `NavHost` to define navigation graph.
    *   Set `startDestination` to Login screen.
    *   Handle navigation actions (e.g., `navController.navigate("dashboard") { popUpTo("login") { inclusive = true } }`).

## Phase 3: Product Management

### 3.1 UI - Products Dashboard (Jetpack Compose)

*   **Create `product` package:** `com.umarndungotech.storekeeper.ui.product`
*   **Create `ProductDashboardScreen.kt`:**
    *   Composable function `ProductDashboardScreen(navController: NavController, productViewModel: ProductViewModel)`.
    *   `Scaffold` with `TopAppBar` and `FloatingActionButton`.
    *   `LazyColumn` to display `ProductCard` for each product.
    *   `ProductCard` composable: Displays product name, quantity, and "View Product" button.
    *   Conditional display: "No Product created..." message if product list is empty.
    *   FAB navigates to "Create Product" screen.

### 3.2 UI - Create Product Screen (Jetpack Compose)

*   **Create `CreateProductScreen.kt`:**
    *   Composable function `CreateProductScreen(navController: NavController, productViewModel: ProductViewModel)`.
    *   `Column` with `TextField` for Product Name, Quantity, Price.
    *   `Image` composable to display selected image or default placeholder.
    *   `Button` to "Select Image" (triggers camera/gallery intent).
    *   `Button` for "Create Product".

### 3.3 UI - View Product Details Screen (Jetpack Compose)

*   **Create `ProductDetailScreen.kt`:**
    *   Composable function `ProductDetailScreen(navController: NavController, productViewModel: ProductViewModel, productId: Int)`.
    *   Fetches product details using `productId`.
    *   Displays Product Name, Quantity, Price, and Image.
    *   `IconButton` for "Update" (navigates to `UpdateProductScreen` with `productId`).
    *   `IconButton` for "Delete" (calls `productViewModel.deleteProduct`).

### 3.4 UI - Update Product Screen (Jetpack Compose)

*   **Create `UpdateProductScreen.kt`:**
    *   Composable function `UpdateProductScreen(navController: NavController, productViewModel: ProductViewModel, productId: Int)`.
    *   Fetches product details using `productId` and pre-fills `TextField`s.
    *   Similar layout to `CreateProductScreen` for editing details and image.
    *   `Button` for "Update Product".

### 3.5 ViewModel

*   **Create `ProductViewModel.kt`:**
    *   Extend `ViewModel`.
    *   Inject `ProductDao` (or a repository).
    *   `getAllProducts(): Flow<List<Product>>`.
    *   `getProductById(id: Int): Product?`.
    *   `insertProduct(product: Product)`.
    *   `updateProduct(product: Product)`.
    *   `deleteProduct(product: Product)`.
    *   `MutableStateFlow` or `LiveData` for UI state (e.g., `_selectedProduct: MutableStateFlow<Product?>`, `_products: StateFlow<List<Product>>`).

## Phase 4: Camera and Image Handling

### 4.1 Camera Integration

*   **Permissions:** Request `CAMERA` and `READ_EXTERNAL_STORAGE` (or `READ_MEDIA_IMAGES` for Android 13+) permissions.
*   **Activity Result API:** Use `rememberLauncherForActivityResult` to launch camera/gallery intents.
*   **Camera Intent:** `Intent(MediaStore.ACTION_IMAGE_CAPTURE)`.
*   **Gallery Intent:** `Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)`.

### 4.2 Image Storage

*   **File Provider:** Set up a `FileProvider` in `AndroidManifest.xml` and `res/xml/file_paths.xml` to securely share image URIs.
*   **Save Image:**
    *   When an image is captured/selected, copy it to the app's private storage (e.g., `context.filesDir`).
    *   Store the URI of this copied image in the `Product` entity.
*   **Load Image:**
    *   Use Coil or Glide library for efficient image loading from URI into `Image` composables.
    *   Handle default placeholder image when `imageUri` is null.

## Phase 5: Finalization and Packaging

### 5.1 Testing

*   **Manual Testing:** Thoroughly test all user flows:
    *   User registration and login.
    *   Creating new products (with and without images).
    *   Viewing product details.
    *   Updating product details (name, quantity, price, image).
    *   Deleting products.
    *   Edge cases (empty fields, invalid input).

### 5.2 UI Polish

*   **Theming:** Apply Material Design 3 theming.
*   **Responsiveness:** Ensure UI adapts well to different screen sizes and orientations.
*   **Accessibility:** Consider content descriptions and other accessibility features.
*   **Error Handling:** Display user-friendly error messages for network issues, invalid input, etc.

### 5.3 Build and Release

*   **Generate Signed APK:** Follow standard Android Studio procedures to generate a release APK.

### 5.4 Documentation

*   **Update `README.md`:**
    *   Add link to the GitHub repository.
    *   Add link to the demo video (after creation).
    *   Include instructions on how to build and run the app.
    *   Provide a link to the release APK.
    *   Ensure clear formatting and readability.
