# Pokedex

This repository contains the first version of a PokeApp where you can:
- LogIn.LogOut.
- Navigate among the PokeApi [PokeApiV2](https://pokeapi.co/)
- Create your customized pokemon groups using Firebase Database as a Backend.

## Project Structure

To make the code better and scalable, I've implemented different technologies, pulling away the bussines logic and the UI interface maded with JetpackCompose. As:

- MVVM AndroidDesignPattern
- Koin DI

So, my application structure consist in basically different layers:

|# Layer | Structure |
| ------------ | ------------ |
| 1     | Bussines Logic, contain all the direct instaniation from the repositories etc  |
| 2      | KOIN DI 1, We can inject the dependencies from the repositories into our viewModels with Koin |
| 3      | ViewModels, To update in real time our UI implementing LiveData |
| 4      | UI Maded with Jetpack Compose |

