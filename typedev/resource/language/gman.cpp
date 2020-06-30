void CGMan :: Spawn()
{
    Precache();

    SET_MODEL( ENT(pev), "models/gman.mdl" );
    UTIL_SetSize(pev, VEC_HUMAN_HULL_MIN, VEC_HUMAN_HULL_MAX);

    pev->solid          = SOLID_SLIDEBOX;
    pev->movetype       = MOVETYPE_STEP;
    m_bloodColor        = DONT_BLEED;
    pev->health         = 100;
    m_flFieldOfView     = 0.5;
    m_MonsterState      = MONSTERSTATE_NONE;

    MonsterInit();
}