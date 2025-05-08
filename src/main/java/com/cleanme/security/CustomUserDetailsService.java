package com.cleanme.security;

public class CustomUserDetailsService {
}

//✅ Spring Security koristi interfejs UserDetailsService da pronađe korisnika na osnovu e-maila ili username-a.
//✅ Tvoja klasa CustomUserDetailsService implementira taj interfejs i govori Springu kako pronaći korisnika u bazi (npr. preko UserRepository).
//✅ Nakon što pronađe korisnika, metoda loadUserByUsername() vraća Springov UserDetails objekat, koji sadrži:
//email kao username
//hashiranu lozinku
//listu prava pristupa (authorities, npr. ROLE_CLIENT)
//✅ Taj UserDetails Spring koristi da:
//validira login (username + password)
//provjeri pristup tokena na svakom requestu (npr. hasRole('CLEANER'))
//✅ Bez ove klase, Spring ne zna kako izgleda tvoja User klasa niti gdje se nalazi.
