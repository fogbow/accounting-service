package cloud.fogbow.accs.api.http.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cloud.fogbow.accs.core.models.orders.OrderState;
import cloud.fogbow.accs.core.models.specs.ComputeSpec;
import cloud.fogbow.accs.core.models.specs.NetworkSpec;
import cloud.fogbow.accs.core.models.specs.VolumeSpec;
import cloud.fogbow.common.exceptions.InvalidParameterException;

public class RecordUtilsTest {

    private static final long RECORD_ID_1 = 1L;
    private static final long RECORD_ID_2 = 2L;
    private static final long RECORD_ID_3 = 3L;
    private static final long RECORD_ID_4 = 4L;
    private static final String ORDER_ID_1 = "orderId1";
    private static final String ORDER_ID_2 = "orderId2";
    private static final String ORDER_ID_3 = "orderId3";
    private static final String ORDER_ID_4 = "orderId4";
    private static final String RESOURCE_TYPE_1 = "compute";
    private static final String RESOURCE_TYPE_2 = "volume";
    private static final String RESOURCE_TYPE_3 = "compute";
    private static final String RESOURCE_TYPE_4 = "network";
    private static final String REQUESTER_1 = "requester1";
    private static final String REQUESTER_2 = "requester2";
    private static final String REQUESTER_3 = "requester3";
    private static final String REQUESTER_4 = "requester4";
    private static final long DURATION_RECORD_1 = 1500000000L;
    private static final long DURATION_RECORD_2 = 2000000000L;
    private static final long DURATION_RECORD_3 = 2500000000L;
    private static final long DURATION_RECORD_4 = 3000000000L;
    private static final String START_DATE_1 = "2000-01-01T00:00:00.000+00:00";
    private static final String START_TIME_1 = "2000-01-01T00:00:00.000+00:00";
    private static final String END_DATE_1 = "2000-01-01T03:03:03.300+00:00";
    private static final String END_TIME_1 = "2000-01-01T03:03:03.300+00:00";
    private static final String START_TIME_2 = "2000-01-01T00:00:00.000+00:00";
    private static final String START_DATE_2 = "2000-01-01T00:00:00.000+00:00";
    private static final String END_DATE_2 = "2000-01-01T03:03:03.300+00:00";
    private static final String END_TIME_2 = "2000-01-01T03:03:03.300+00:00";
    private static final String START_TIME_3 = "2000-01-01T00:00:00.000+00:00";
    private static final String START_DATE_3 = "2000-01-01T00:00:00.000+00:00";
    private static final String END_DATE_3 = "2000-01-01T03:03:03.300+00:00";
    private static final String END_TIME_3 = "2000-01-01T03:03:03.300+00:00";
    private static final String START_DATE_4 = "2000-01-01T00:00:00.000+00:00";
    private static final String START_TIME_4 = "2000-01-01T00:00:00.000+00:00";
    private static final String END_DATE_4 = "2000-01-01T03:03:03.300+00:00";
    private static final String END_TIME_4 = "2000-01-01T03:03:03.300+00:00";
    private static final long SPEC_ID_1 = 1L;
    private static final long SPEC_ID_2 = 2L;
    private static final long SPEC_ID_3 = 3L;
    private static final long SPEC_ID_4 = 4L;
    private static final int RAM_RECORD_1 = 4;
    private static final int VCPU_RECORD_1 = 2;
    private static final int SIZE_RECORD_2 = 100;
    private static final int VCPU_RECORD_3 = 4;
    private static final int RAM_RECORD_3 = 8;
    private static final String CIDR_RECORD_4 = "10.0.1.0/24";
    private static final String ALLOCATION_MODE_RECORD_4 = "dynamic";
    private static final String STATE_RECORD_1 = "FULFILLED";
    private static final String STATE_RECORD_2 = "OPEN";
    private static final String STATE_RECORD_3 = "OPEN";
    private static final String STATE_RECORD_4 = "OPEN";
    private AccsApiUtils recordUtils;

    // test case: When calling the getRecordsFromString method, it must parse the given 
    // String and return a List containing Record instances of the correct types containing 
    // the data passed as argument.
    @Test
    public void testGetRecordsFromString() throws InvalidParameterException {
        this.recordUtils = new AccsApiUtils();

        RecordStringBuilder builder = new RecordStringBuilder();
        
        builder.addRecordString(new ComputeRecordString(RECORD_ID_1, ORDER_ID_1, RESOURCE_TYPE_1, 
                SPEC_ID_1, VCPU_RECORD_1, RAM_RECORD_1, REQUESTER_1, START_TIME_1, START_DATE_1, END_DATE_1, 
                END_TIME_1, DURATION_RECORD_1, STATE_RECORD_1));
        builder.addRecordString(new VolumeRecordString(RECORD_ID_2, ORDER_ID_2, RESOURCE_TYPE_2, 
                SPEC_ID_2, SIZE_RECORD_2, REQUESTER_2, START_TIME_2, START_DATE_2, END_DATE_2, 
                END_TIME_2, DURATION_RECORD_2, STATE_RECORD_2));
        builder.addRecordString(new ComputeRecordString(RECORD_ID_3, ORDER_ID_3, RESOURCE_TYPE_3, 
                SPEC_ID_3, VCPU_RECORD_3, RAM_RECORD_3, REQUESTER_3, START_TIME_3, START_DATE_3, END_DATE_3, 
                END_TIME_3, DURATION_RECORD_3, STATE_RECORD_3));
        builder.addRecordString(new NetworkRecordString(RECORD_ID_4, ORDER_ID_4, RESOURCE_TYPE_4, 
                SPEC_ID_4, CIDR_RECORD_4, ALLOCATION_MODE_RECORD_4, REQUESTER_4, START_TIME_4, START_DATE_4, END_DATE_4, 
                END_TIME_4, DURATION_RECORD_4, STATE_RECORD_4));
        
        String recordsString = builder.build();
        List<Record> records = this.recordUtils.getRecordsFromString(recordsString);
        
        Record record1 = records.get(0);
        
        assertEquals(RECORD_ID_1, record1.getId().longValue());
        assertEquals(ORDER_ID_1, record1.getOrderId());
        assertEquals(RESOURCE_TYPE_1, record1.getResourceType());
        assertEquals(SPEC_ID_1, record1.getSpec().getId().longValue());
        assertEquals(VCPU_RECORD_1, ((ComputeSpec) record1.getSpec()).getvCpu());
        assertEquals(RAM_RECORD_1, ((ComputeSpec) record1.getSpec()).getRam());
        assertEquals(REQUESTER_1, record1.getRequester());
        assertEquals(DURATION_RECORD_1, record1.getDuration());
        assertEquals(OrderState.valueOf(STATE_RECORD_1), record1.getState());
        
        Record record2 = records.get(1);
        
        assertEquals(RECORD_ID_2, record2.getId().longValue());
        assertEquals(ORDER_ID_2, record2.getOrderId());
        assertEquals(RESOURCE_TYPE_2, record2.getResourceType());
        assertEquals(SPEC_ID_2, record2.getSpec().getId().longValue());
        assertEquals(SIZE_RECORD_2, ((VolumeSpec) record2.getSpec()).getSize());
        assertEquals(REQUESTER_2, record2.getRequester());
        assertEquals(DURATION_RECORD_2, record2.getDuration());
        assertEquals(OrderState.valueOf(STATE_RECORD_2), record2.getState());
        
        Record record3 = records.get(2);
        
        assertEquals(RECORD_ID_3, record3.getId().longValue());
        assertEquals(ORDER_ID_3, record3.getOrderId());
        assertEquals(RESOURCE_TYPE_3, record3.getResourceType());
        assertEquals(SPEC_ID_3, record3.getSpec().getId().longValue());
        assertEquals(VCPU_RECORD_3, ((ComputeSpec) record3.getSpec()).getvCpu());
        assertEquals(RAM_RECORD_3, ((ComputeSpec) record3.getSpec()).getRam());
        assertEquals(REQUESTER_3, record3.getRequester());
        assertEquals(DURATION_RECORD_3, record3.getDuration());
        assertEquals(OrderState.valueOf(STATE_RECORD_3), record3.getState());
        
        Record record4 = records.get(3);
        
        assertEquals(RECORD_ID_4, record4.getId().longValue());
        assertEquals(ORDER_ID_4, record4.getOrderId());
        assertEquals(RESOURCE_TYPE_4, record4.getResourceType());
        assertEquals(SPEC_ID_4, record4.getSpec().getId().longValue());
        assertEquals(CIDR_RECORD_4, ((NetworkSpec) record4.getSpec()).getCidr());
        assertEquals(ALLOCATION_MODE_RECORD_4, ((NetworkSpec) record4.getSpec()).getAllocationMode().getValue());
        assertEquals(REQUESTER_4, record4.getRequester());
        assertEquals(DURATION_RECORD_4, record4.getDuration());
        assertEquals(OrderState.valueOf(STATE_RECORD_4), record4.getState());
    }
    
    // test case: When calling the getRecordsFromString method and the given String
    // contains no record data, it must return an empty list.
    @Test
    public void testGetRecordsFromStringNoRecords() throws InvalidParameterException {
        this.recordUtils = new AccsApiUtils();
        RecordStringBuilder builder = new RecordStringBuilder();
        String recordsString = builder.build();
        
        List<Record> records = this.recordUtils.getRecordsFromString(recordsString);
        
        assertTrue(records.isEmpty());
    }
    
    // test case: When calling the getRecordsFromString method and the given String
    // contains records data from types other than "compute", "volume" and "network", 
    // it must throw an InvalidParameterException.
    @Test(expected = InvalidParameterException.class)
    public void testGetRecordsFromStringInvalidRecordType() 
            throws InvalidParameterException {
        this.recordUtils = new AccsApiUtils();
        
        String recordString = String.format(""
                + "{"
                + "    \"id\": %d,"
                + "    \"orderId\": \"%s\","
                + "    \"resourceType\": \"%s\","
                + "    \"spec\": {"
                + "        \"id\": %d,"
                + "        \"info1\": info1,"
                + "        \"info2\": info2"
                + "    },"
                + "    \"requester\": \"%s\","
                + "    \"startTime\": \"%s\","
                + "    \"startDate\": \"%s\","
                + "    \"endDate\": \"%s\","
                + "    \"endTime\": \"%s\","
                + "    \"duration\": %d,"
                + "    \"state\": \"%s\""
                + "}", RECORD_ID_1, ORDER_ID_1, "othertype", SPEC_ID_1, REQUESTER_1, 
                START_TIME_1, START_DATE_1, END_DATE_1, END_TIME_1, DURATION_RECORD_1, 
                STATE_RECORD_1);
        
        String recordsString = String.format("[%s]", recordString);
        
        this.recordUtils.getRecordsFromString(recordsString);
    }
    
    // test case: When calling the getRecordsFromString method and the given String
    // contains compute records data with missing properties, it must throw an
    // InvalidParameterException.
    @Test(expected = InvalidParameterException.class)
    public void testGetRecordsFromStringInvalidComputeRecord() 
            throws InvalidParameterException {
        this.recordUtils = new AccsApiUtils();
        
        String recordString = String.format(""
                + "{"
                + "    \"id\": %d,"
                + "    \"orderId\": \"%s\","
                + "    \"resourceType\": \"%s\","
                + "    \"spec\": {"
                + "        \"id\": %d,"
                + "        \"vCpu\": \"%d\","
                + "        \"ram\": \"%d\""
                + "    },"
                + "    \"requester\": \"%s\","
                + "    \"startDate\": \"%s\","
                + "    \"endDate\": \"%s\","
                + "    \"endTime\": \"%s\","
                + "    \"duration\": %d,"
                + "    \"state\": \"%s\""
                + "}", RECORD_ID_1, ORDER_ID_1, RESOURCE_TYPE_1, SPEC_ID_1, VCPU_RECORD_1, 
                RAM_RECORD_1, REQUESTER_1, START_DATE_1, END_DATE_1, END_TIME_1, 
                DURATION_RECORD_1, STATE_RECORD_1);
        
        String recordsString = String.format("[%s]", recordString);
        
        this.recordUtils.getRecordsFromString(recordsString);
    }
    
    // test case: When calling the getRecordsFromString method and the given String
    // contains volume records data with missing properties, it must throw an
    // InvalidParameterException.
    @Test(expected = InvalidParameterException.class)
    public void testGetRecordsFromStringInvalidVolumeRecord() 
            throws InvalidParameterException {
        this.recordUtils = new AccsApiUtils();
        
        String recordString = String.format(""
                + "{"
                + "    \"id\": %d,"
                + "    \"orderId\": \"%s\","
                + "    \"resourceType\": \"%s\","
                + "    \"spec\": {"
                + "        \"id\": %d,"
                + "        \"size\": \"%d\""
                + "    },"
                + "    \"requester\": \"%s\","
                + "    \"startDate\": \"%s\","
                + "    \"endDate\": \"%s\","
                + "    \"endTime\": \"%s\","
                + "    \"duration\": %d,"
                + "    \"state\": \"%s\""
                + "}", RECORD_ID_2, ORDER_ID_2, RESOURCE_TYPE_2, 
                SPEC_ID_2, SIZE_RECORD_2, REQUESTER_2, START_DATE_2, END_DATE_2, 
                END_TIME_2, DURATION_RECORD_2, STATE_RECORD_2);
        
        String recordsString = String.format("[%s]", recordString);
        
        this.recordUtils.getRecordsFromString(recordsString);
    }
    
    // test case: When calling the getRecordsFromString method and the given String
    // contains network records data with missing properties, it must throw an
    // InvalidParameterException.
    @Test(expected = InvalidParameterException.class)
    public void testGetRecordsFromStringInvalidNetworkRecord() 
            throws InvalidParameterException {
        this.recordUtils = new AccsApiUtils();
        
        String recordString = String.format(""
                + "{"
                + "    \"id\": %d,"
                + "    \"orderId\": \"%s\","
                + "    \"resourceType\": \"%s\","
                + "    \"spec\": {"
                + "        \"id\": %d,"
                + "        \"cidr\": \"%s\","
                + "        \"allocationMode\": \"%s\""
                + "    },"
                + "    \"requester\": \"%s\","
                + "    \"startDate\": \"%s\","
                + "    \"endDate\": \"%s\","
                + "    \"endTime\": \"%s\","
                + "    \"duration\": %d,"
                + "    \"state\": \"%s\""
                + "}", RECORD_ID_4, ORDER_ID_4, RESOURCE_TYPE_4, 
                SPEC_ID_4, CIDR_RECORD_4, ALLOCATION_MODE_RECORD_4, REQUESTER_4, 
                START_DATE_4, END_DATE_4, END_TIME_4, DURATION_RECORD_4, STATE_RECORD_4);
        
        String recordsString = String.format("[%s]", recordString);
        
        this.recordUtils.getRecordsFromString(recordsString);
    }

    private abstract class RecordString {
        protected long id;
        protected String orderId;
        protected String resourceType;
        protected long specId;
        protected String requester;
        protected String startTime;
        protected String startDate;
        protected String endDate;
        protected String endTime;
        protected long duration;
        protected String state;
        
        public RecordString(long id, String orderId, String resourceType, 
                long specId, String requester, String startTime, String startDate, 
                String endDate, String endTime, long duration, String state) {
            this.id = id;
            this.orderId = orderId;
            this.resourceType = resourceType;
            this.specId = specId;
            this.requester = requester;
            this.startTime = startTime;
            this.startDate = startDate;
            this.endDate = endDate;
            this.endTime = endTime;
            this.duration = duration;
            this.state = state;
        }
        
        abstract String getString();
    }
    
    private class ComputeRecordString extends RecordString {
        private int vCPU;
        private int ram;
        
        public ComputeRecordString(long id, String orderId, String resourceType, 
                long specId, int vCPU, int ram, String requester, String startTime, 
                String startDate, String endDate, String endTime, long duration, String state) {
            super(id, orderId, resourceType, specId, requester, startTime, startDate, 
                    endDate, endTime, duration, state);
            this.vCPU = vCPU;
            this.ram = ram;
        }

        public String getString() {
            String recordString = String.format(""
                    + "{"
                    + "    \"id\": %d,"
                    + "    \"orderId\": \"%s\","
                    + "    \"resourceType\": \"%s\","
                    + "    \"spec\": {"
                    + "        \"id\": %d,"
                    + "        \"vCpu\": \"%d\","
                    + "        \"ram\": \"%d\""
                    + "    },"
                    + "    \"requester\": \"%s\","
                    + "    \"startTime\": \"%s\","
                    + "    \"startDate\": \"%s\","
                    + "    \"endDate\": \"%s\","
                    + "    \"endTime\": \"%s\","
                    + "    \"duration\": %d,"
                    + "    \"state\": \"%s\""
                    + "}", id, orderId, resourceType, specId, vCPU, ram, requester, 
                    startTime, startDate, endDate, endTime, duration, state);
            
            return recordString;
        }
    }
    
    private class VolumeRecordString extends RecordString {
        private int size;

        public VolumeRecordString(long id, String orderId, String resourceType, 
                long specId, int size, String requester, String startTime, 
                String startDate, String endDate, String endTime, long duration, String state) {
            super(id, orderId, resourceType, specId, requester, startTime, startDate, 
                    endDate, endTime, duration, state);
            this.size = size;
        }
        
        public String getString() {
            String recordString = String.format(""
                    + "{"
                    + "    \"id\": %d,"
                    + "    \"orderId\": \"%s\","
                    + "    \"resourceType\": \"%s\","
                    + "    \"spec\": {"
                    + "        \"id\": %d,"
                    + "        \"size\": \"%d\""
                    + "    },"
                    + "    \"requester\": \"%s\","
                    + "    \"startTime\": \"%s\","
                    + "    \"startDate\": \"%s\","
                    + "    \"endDate\": \"%s\","
                    + "    \"endTime\": \"%s\","
                    + "    \"duration\": %d,"
                    + "    \"state\": \"%s\""
                    + "}", id, orderId, resourceType, specId, size, requester, 
                    startTime, startDate, endDate, endTime, duration, state);
            
            return recordString;
        }
    }
    
    private class NetworkRecordString extends RecordString {
        private String cidr;
        private String allocationMode;

        public NetworkRecordString(long id, String orderId, String resourceType, 
                long specId, String cidr, String allocationMode, String requester, String startTime, 
                String startDate, String endDate, String endTime, long duration, String state) {
            super(id, orderId, resourceType, specId, requester, startTime, startDate, 
                    endDate, endTime, duration, state);
            this.cidr = cidr;
            this.allocationMode = allocationMode;
        }
        
        public String getString() {
            String recordString = String.format(""
                    + "{"
                    + "    \"id\": %d,"
                    + "    \"orderId\": \"%s\","
                    + "    \"resourceType\": \"%s\","
                    + "    \"spec\": {"
                    + "        \"id\": %d,"
                    + "        \"cidr\": \"%s\","
                    + "        \"allocationMode\": \"%s\""
                    + "    },"
                    + "    \"requester\": \"%s\","
                    + "    \"startTime\": \"%s\","
                    + "    \"startDate\": \"%s\","
                    + "    \"endDate\": \"%s\","
                    + "    \"endTime\": \"%s\","
                    + "    \"duration\": %d,"
                    + "    \"state\": \"%s\""
                    + "}", id, orderId, resourceType, specId, cidr, allocationMode, 
                    requester, startTime, startDate, endDate, endTime, duration, state);
            
            return recordString;
        }
    }
    
    private class RecordStringBuilder {
        private List<RecordString> recordStrings;
        
        public RecordStringBuilder() {
            recordStrings = new ArrayList<RecordString>();
        }
        
        public void addRecordString(RecordString recordString) {
            recordStrings.add(recordString);
        }
        
        public String build() {
            List<String> strings = new ArrayList<String>();
            
            for (RecordString string : recordStrings) {
                strings.add(string.getString());
            }
            
            return String.format("[%s]", String.join(",", strings));
        }
    }
}
