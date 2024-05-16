# Test

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityGroupDTO {
    private String entityType;
    private String firstName;
    private String lastName;
    private String firmName;
    private Integer refNo;
    private List<PhoneDTO> phones;
    private List<AddressDTO> addresses;

    public EntityGroupDTO(String entityType, String firstName, String lastName, String firmName, Integer refNo) {
        this.entityType = entityType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.firmName = firmName;
        this.refNo = refNo;
        this.phones = new ArrayList<>();
        this.addresses = new ArrayList<>();
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public Integer getRefNo() {
        return refNo;
    }

    public void setRefNo(Integer refNo) {
        this.refNo = refNo;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }
}

class PhoneDTO {
    private String phType;
    private String phNum;

    public PhoneDTO(String phType, String phNum) {
        this.phType = phType;
        this.phNum = phNum;
    }

    public String getPhType() {
        return phType;
    }

    public void setPhType(String phType) {
        this.phType = phType;
    }

    public String getPhNum() {
        return phNum;
    }

    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }
}

class AddressDTO {
    private String addressType;
    private String addLine1;
    private String addLine2;

    public AddressDTO(String addressType, String addLine1, String addLine2) {
        this.addressType = addressType;
        this.addLine1 = addLine1;
        this.addLine2 = addLine2;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
    }
}

public class EntityDTO {
    private String entityType;
    private String firstName;
    private String lastName;
    private String firmName;
    private Integer refNo;
    private String addLine1;
    private String addLine2;
    private String addressType;
    private String phType;
    private String phNum;

    public EntityDTO() {
        // Default constructor
    }

    public EntityDTO(String entityType, String firstName, String lastName, String firmName, Integer refNo, String addLine1, String addLine2, String addressType, String phType, String phNum) {
        this.entityType = entityType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.firmName = firmName;
        this.refNo = refNo;
        this.addLine1 = addLine1;
        this.addLine2 = addLine2;
        this.addressType = addressType;
        this.phType = phType;
        this.phNum = phNum;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public Integer getRefNo() {
        return refNo;
    }

    public void setRefNo(Integer refNo) {
        this.refNo = refNo;
    }

    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getPhType() {
        return phType;
    }

    public void setPhType(String phType) {
        this.phType = phType;
    }

    public String getPhNum() {
        return phNum;
    }

    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }

    public static List<EntityGroupDTO> groupByEntityType(List<EntityDTO> entityDTOList) {
        Map<String, EntityGroupDTO> entityGroupMap = new HashMap<>();

        entityDTOList.forEach(entityDTO -> {
            String entityType = entityDTO.getEntityType();

            if (!entityGroupMap.containsKey(entityType)) {
                EntityGroupDTO entityGroupDTO = new EntityGroupDTO(entityType, entityDTO.getFirstName(),
                        entityDTO.getLastName(), entityDTO.getFirmName(), entityDTO.getRefNo());
                entityGroupMap.put(entityType, entityGroupDTO);
            }

            EntityGroupDTO entityGroupDTO = entityGroupMap.get(entityType);

            String phType = entityDTO.getPhType();
            if (!entityGroupDTO.getPhones().stream().anyMatch(phone -> phone.getPhType().equals(phType))) {
                PhoneDTO phoneDTO = new PhoneDTO(phType, entityDTO.getPhNum());
                entityGroupDTO.getPhones().add(phoneDTO);
            }

            String addressType = entityDTO.getAddressType();
            if (!entityGroupDTO.getAddresses().stream().anyMatch(address -> address.getAddressType().equals(addressType))) {
                AddressDTO addressDTO = new AddressDTO(addressType, entityDTO.getAddLine1(), entityDTO.getAddLine2());
                entityGroupDTO.getAddresses().add(addressDTO);
            }
        });

        return List.copyOf(entityGroupMap.values());
    }
}

    public String getPhNum() {
        return phNum;
    }

    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }
}

}
