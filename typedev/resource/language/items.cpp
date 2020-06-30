#include "skill.h"
#include "items.h"
#include "gamerules.h"

extern int gmsgItemPickup;

class CWorldItem : public CBaseEntity
{
public:
    void    KeyValue(KeyValueData *pkvd ); 
    void    Spawn( void );
    int     m_iType;
};

LINK_ENTITY_TO_CLASS(world_items, CWorldItem);