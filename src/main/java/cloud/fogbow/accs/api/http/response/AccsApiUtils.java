package cloud.fogbow.accs.api.http.response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.core.models.orders.OrderState;
import cloud.fogbow.accs.core.models.specs.ComputeSpec;
import cloud.fogbow.accs.core.models.specs.NetworkSpec;
import cloud.fogbow.accs.core.models.specs.OrderSpec;
import cloud.fogbow.accs.core.models.specs.VolumeSpec;
import cloud.fogbow.common.exceptions.InvalidParameterException;

public class AccsApiUtils {
	private static final String RESOURCE_TYPE_KEY = "resourceType";
	private static final String COMPUTE_RESOURCE = "compute";
	private static final String VOLUME_RESOURCE = "volume";
	private static final String NETWORK_RESOURCE = "network";
	
	public AccsApiUtils() {
	}
	
	public List<Record> getRecordsFromString(String recordsString) throws InvalidParameterException {
    	ArrayList<Record> recordList = new ArrayList<Record>();
    	Gson gson = new Gson();
    	
        ArrayList<LinkedTreeMap<String, Object>> rawRecordsList = 
        		gson.fromJson(recordsString, ArrayList.class);

        for (LinkedTreeMap<String, Object> rawRecord : rawRecordsList) {
        	Record record;
        	String recordType = (String) rawRecord.get(RESOURCE_TYPE_KEY);
        	
        	switch(recordType) {
        		case COMPUTE_RESOURCE: record = getComputeRecord(rawRecord); break;
        		case VOLUME_RESOURCE: record = getVolumeRecord(rawRecord); break;
        		case NETWORK_RESOURCE: record = getNetworkRecord(rawRecord); break;
        		default: throw new InvalidParameterException(
        		        String.format(Messages.Exception.INVALID_RECORD_TYPE, recordType));
            }
        	
        	recordList.add(record);
        }
        
        return recordList;
	}
	
    private ComputeRecord getComputeRecord(LinkedTreeMap<String, Object> rawRecord) throws InvalidParameterException {
    	Gson gson = new Gson();
		String jsonRepr = gson.toJson((LinkedTreeMap<String, Object>) rawRecord);
		ComputeRecord computeRecord = gson.fromJson(jsonRepr, ComputeRecord.class);
		computeRecord.validate();
		return computeRecord;
	}
	
	private VolumeRecord getVolumeRecord(LinkedTreeMap<String, Object> rawRecord) throws InvalidParameterException {
		Gson gson = new Gson();
		String jsonRepr = gson.toJson((LinkedTreeMap<String, Object>) rawRecord);
		VolumeRecord volumeRecord = gson.fromJson(jsonRepr, VolumeRecord.class);
		volumeRecord.validate();
		return volumeRecord;
	}

    private Record getNetworkRecord(LinkedTreeMap<String, Object> rawRecord) throws InvalidParameterException {
    	Gson gson = new Gson();
        String jsonRepr = gson.toJson((LinkedTreeMap<String, Object>) rawRecord);
        NetworkRecord networkRecord = gson.fromJson(jsonRepr, NetworkRecord.class);
        networkRecord.validate();
        return networkRecord;
    }
    
	public class ComputeRecord extends Record {
		private ComputeSpec spec;
		
		public ComputeRecord(Long id, String orderId, String resourceType, ComputeSpec spec, String requester, Timestamp startTime,
				  Timestamp startDate, Timestamp endTime, Timestamp endDate, long duration, OrderState state) throws InvalidParameterException {
			super(id, orderId, resourceType, requester, startTime, startDate, endTime, endDate, duration, state);
			this.spec = spec;
		}
		
		@Override
		public ComputeSpec getSpec() {
			return spec;
		}

		@Override
		public void setSpec(OrderSpec orderSpec) {
			this.spec = (ComputeSpec) orderSpec;
		}

        public void validate() throws InvalidParameterException {
            checkRecordPropertyIsNotNull("resourceType", getResourceType());
            checkRecordPropertyIsNotNull("spec", getSpec());
            checkRecordPropertyIsNotNull("startTime", getStartTime());
            checkRecordPropertyIsNotNull("startDate", getStartDate());
        }
	}
	
	public class VolumeRecord extends Record {
		private VolumeSpec spec;
		
		public VolumeRecord(Long id, String orderId, String resourceType, VolumeSpec spec, String requester, Timestamp startTime,
				  Timestamp startDate, Timestamp endTime, Timestamp endDate, long duration, OrderState state) {
			super(id, orderId, resourceType, requester, startTime, startDate, endTime, endDate, duration, state);
			this.spec = spec;
		}
		
		@Override
		public VolumeSpec getSpec() {
			return spec;
		}

		@Override
		public void setSpec(OrderSpec orderSpec) {
			this.spec = (VolumeSpec) orderSpec;
		}

        public void validate() throws InvalidParameterException {
            checkRecordPropertyIsNotNull("resourceType", getResourceType());
            checkRecordPropertyIsNotNull("spec", getSpec());
            checkRecordPropertyIsNotNull("startTime", getStartTime());
            checkRecordPropertyIsNotNull("startDate", getStartDate());
        }
	}
	
    public class NetworkRecord extends Record {
        private NetworkSpec spec;

        public NetworkRecord(Long id, String orderId, String resourceType, NetworkSpec spec, String requester,
                Timestamp startTime, Timestamp startDate, Timestamp endTime, Timestamp endDate, long duration,
                OrderState state) {
            super(id, orderId, resourceType, requester, startTime, startDate, endTime, endDate, duration, state);
            this.spec = spec;
        }

        @Override
        public NetworkSpec getSpec() {
            return spec;
        }

        @Override
        public void setSpec(OrderSpec orderSpec) {
            this.spec = (NetworkSpec) orderSpec;
        }

        public void validate() throws InvalidParameterException {
            checkRecordPropertyIsNotNull("resourceType", getResourceType());
            checkRecordPropertyIsNotNull("spec", getSpec());
            checkRecordPropertyIsNotNull("startTime", getStartTime());
            checkRecordPropertyIsNotNull("startDate", getStartDate());
        }
    }
    
    private static void checkRecordPropertyIsNotNull(String propertyName, Object o) 
            throws InvalidParameterException {
        if (o == null) {
            throw new InvalidParameterException(
            		String.format(Messages.Exception.INVALID_RECORD_PROPERTY, propertyName));
        }
    }
    
    public Record mountResponseRecord(cloud.fogbow.accs.core.models.Record dbRecord) throws InvalidParameterException {
    	OrderSpec spec = dbRecord.getSpec();
    	                                    
    	if (spec instanceof ComputeSpec) {
    		return new ComputeRecord(dbRecord.getId(), dbRecord.getOrderId(), dbRecord.getResourceType(), (ComputeSpec) dbRecord.getSpec(),
                    dbRecord.getRequestingMember(), dbRecord.getStartTime(), dbRecord.getStartDate(),
                    dbRecord.getEndTime(), dbRecord.getEndDate(), dbRecord.getDuration(), dbRecord.getState());
    	} else if (spec instanceof VolumeSpec) {
    		return new VolumeRecord(dbRecord.getId(), dbRecord.getOrderId(), dbRecord.getResourceType(), (VolumeSpec) dbRecord.getSpec(),
                    dbRecord.getRequestingMember(), dbRecord.getStartTime(), dbRecord.getStartDate(),
                    dbRecord.getEndTime(), dbRecord.getEndDate(), dbRecord.getDuration(), dbRecord.getState());
    	} else if (spec instanceof NetworkSpec) {
    		return new NetworkRecord(dbRecord.getId(), dbRecord.getOrderId(), dbRecord.getResourceType(), (NetworkSpec) dbRecord.getSpec(),
                    dbRecord.getRequestingMember(), dbRecord.getStartTime(), dbRecord.getStartDate(),
                    dbRecord.getEndTime(), dbRecord.getEndDate(), dbRecord.getDuration(), dbRecord.getState());
    	} else {
    		// TODO error
    		return null;
    	}
    }
}
