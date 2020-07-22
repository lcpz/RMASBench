# Known bugs

- [ ] The scoring function should not improve when fires are *indomable*. A fire
  is *indomable* when it cannot be contained by the available fire fighters.
  In these cases, the score should be reduced by a factor that is proportional
  to the percentage of burned-down buildings, and the percentage of buildings
  that reported significant damage.
