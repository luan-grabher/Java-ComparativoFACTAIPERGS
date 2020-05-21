package Model.Entities;

import tpsdb.Model.Entities.Associate;
import tpsdb.Model.Entities.Contract;

public class MonthContract {
    private String name;
    private IpergsLcto ipergs;
    private Contract contract;
    private Associate associate;

    public MonthContract() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IpergsLcto getIpergs() {
        return ipergs;
    }

    public void setIpergs(IpergsLcto ipergs) {
        this.ipergs = ipergs;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Associate getAssociate() {
        return associate;
    }

    public void setAssociate(Associate associate) {
        this.associate = associate;
    }
    
    
}
