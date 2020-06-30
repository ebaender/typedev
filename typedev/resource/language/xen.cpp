#define XEN_PLANT_GLOW_SPRITE       "sprites/flare3.spr"
#define XEN_PLANT_HIDE_TIME         5


class CActAnimating : public CBaseAnimating
{
public:
    void            SetActivity( Activity act );
    inline Activity GetActivity( void ) { return m_Activity; }

    virtual int	ObjectCaps( void ) { return CBaseAnimating :: ObjectCaps() & ~FCAP_ACROSS_TRANSITION; }

    virtual int	Save( CSave &save );
    virtual int	Restore( CRestore &restore );
    static  TYPEDESCRIPTION m_SaveData[];

private:
    Activity    m_Activity;
};