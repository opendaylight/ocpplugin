package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultRes;
import org.opendaylight.yangtools.yang.binding.DataObject;
import java.util.HashMap;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getfaultoutput.Obj;
import org.opendaylight.yangtools.concepts.Builder;
import java.util.Objects;
import java.util.List;
import java.util.Collections;
import java.util.Map;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput
 *
 */
public class GetFaultOutputBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput> {

    private List<Obj> _obj;
    private GetFaultRes _result;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>> augmentation = Collections.emptyMap();

    public GetFaultOutputBuilder() {
    }
    public GetFaultOutputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultOutput arg) {
        this._obj = arg.getObj();
    }

    public GetFaultOutputBuilder(GetFaultOutput base) {
        this._obj = base.getObj();
        this._result = base.getResult();
        if (base instanceof GetFaultOutputImpl) {
            GetFaultOutputImpl impl = (GetFaultOutputImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }

    /**
     *Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultOutput</li>
     * </ul>
     *
     * @param arg grouping object
     * @throws IllegalArgumentException if given argument is none of valid types
    */
    public void fieldsFrom(DataObject arg) {
        boolean isValidArg = false;
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultOutput) {
            this._obj = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultOutput)arg).getObj();
            isValidArg = true;
        }
        if (!isValidArg) {
            throw new IllegalArgumentException(
              "expected one of: [org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultOutput] \n" +
              "but was: " + arg
            );
        }
    }

    public List<Obj> getObj() {
        return _obj;
    }
    
    public GetFaultRes getResult() {
        return _result;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public GetFaultOutputBuilder setObj(final List<Obj> value) {
        this._obj = value;
        return this;
    }
    
     
    public GetFaultOutputBuilder setResult(final GetFaultRes value) {
        this._result = value;
        return this;
    }
    
    public GetFaultOutputBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public GetFaultOutputBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public GetFaultOutput build() {
        return new GetFaultOutputImpl(this);
    }

    private static final class GetFaultOutputImpl implements GetFaultOutput {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput.class;
        }

        private final List<Obj> _obj;
        private final GetFaultRes _result;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>> augmentation = Collections.emptyMap();

        private GetFaultOutputImpl(GetFaultOutputBuilder base) {
            this._obj = base.getObj();
            this._result = base.getResult();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
        }

        @Override
        public List<Obj> getObj() {
            return _obj;
        }
        
        @Override
        public GetFaultRes getResult() {
            return _result;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
            if (augmentationType == null) {
                throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
            }
            return (E) augmentation.get(augmentationType);
        }

        private int hash = 0;
        private volatile boolean hashValid = false;
        
        @Override
        public int hashCode() {
            if (hashValid) {
                return hash;
            }
        
            final int prime = 31;
            int result = 1;
            result = prime * result + Objects.hashCode(_obj);
            result = prime * result + Objects.hashCode(_result);
            result = prime * result + Objects.hashCode(augmentation);
        
            hash = result;
            hashValid = true;
            return result;
        }

        @Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DataObject)) {
                return false;
            }
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput)obj;
            if (!Objects.equals(_obj, other.getObj())) {
                return false;
            }
            if (!Objects.equals(_result, other.getResult())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                GetFaultOutputImpl otherImpl = (GetFaultOutputImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>> e : augmentation.entrySet()) {
                    if (!e.getValue().equals(other.getAugmentation(e.getKey()))) {
                        return false;
                    }
                }
                // .. and give the other one the chance to do the same
                if (!obj.equals(this)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public java.lang.String toString() {
            java.lang.String name = "GetFaultOutput [";
            java.lang.StringBuilder builder = new java.lang.StringBuilder (name);
            if (_obj != null) {
                builder.append("_obj=");
                builder.append(_obj);
                builder.append(", ");
            }
            if (_result != null) {
                builder.append("_result=");
                builder.append(_result);
            }
            final int builderLength = builder.length();
            final int builderAdditionalLength = builder.substring(name.length(), builderLength).length();
            if (builderAdditionalLength > 2 && !builder.substring(builderLength - 2, builderLength).equals(", ")) {
                builder.append(", ");
            }
            builder.append("augmentation=");
            builder.append(augmentation.values());
            return builder.append(']').toString();
        }
    }

}
