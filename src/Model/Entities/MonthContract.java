package Model.Entities;

import tpsdb.Model.Entities.Associado;
import tpsdb.Model.Entities.Contrato;

public class MonthContract {
    private IpergsLcto ipergs;
    private Contrato contract;
    private Associado associate;

    public MonthContract() {
    }


    public IpergsLcto getIpergs() {
        return ipergs;
    }

    public void setIpergs(IpergsLcto ipergs) {
        this.ipergs = ipergs;
    }

    public Contrato getContract() {
        return contract;
    }

    public void setContract(Contrato contract) {
        this.contract = contract;
    }

    public Associado getAssociate() {
        return associate;
    }

    public void setAssociate(Associado associate) {
        this.associate = associate;
    }
    
    
}
