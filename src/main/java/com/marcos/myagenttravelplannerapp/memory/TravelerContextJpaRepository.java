package com.marcos.myagenttravelplannerapp.memory;

import org.springframework.data.jpa.repository.JpaRepository;

interface TravelerContextJpaRepository extends JpaRepository<TravelerContextEntity, String> {}