# Dungeon Crawler

A Java-based dungeon crawler game with randomly generated levels, turn-based combat, and persistent progress.

## Features

- Randomly generated dungeons with multiple rooms and corridors
- Turn-based combat system
- Multiple levels with progress saving
- Enemy encounters
- Keyboard controls for movement and combat

## Controls

- Movement: WASD keys
  - W: Move Up
  - A: Move Left
  - S: Move Down
  - D: Move Right
- Q: Attack
- E: Use Item
- N: Next Level (when all enemies are defeated)
- B: Previous Level
- "Give Up" button to exit game

## How to Play

1. Compile and run the game:
   ```bash
   javac DungeonCrawler.java
   java DungeonCrawler
   ```

2. The game will automatically open in your default web browser at `http://localhost:8080`

3. Navigate through the dungeon using WASD keys
4. Engage in combat by moving next to enemies
5. Clear all enemies in a level to reveal the stairs to the next level
6. Use 'N' to proceed to the next level and 'B' to return to previous levels

## Combat System

- Turn-based battles when encountering enemies
- Two attack moves:
  - Swing: Consistent damage (10-25)
  - Slash: Variable damage (5-25)
- Health bars for both player and enemy
- Combat log showing all actions

## Game Progress

- Progress is automatically saved after each action
- Defeated enemies stay defeated when returning to previous levels
- Clear game state by using the "Give Up" button

## Requirements

- Java Runtime Environment (JRE) 8 or higher
- Web browser
- Port 8080 available on your system
