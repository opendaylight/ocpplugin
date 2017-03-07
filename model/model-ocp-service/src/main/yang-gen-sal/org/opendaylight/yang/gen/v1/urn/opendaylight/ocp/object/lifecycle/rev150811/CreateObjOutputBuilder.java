package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjGlobRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.createobjoutput.Param;
import java.util.Objects;
import java.util.List;
import java.util.Collections;
import java.util.Map;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput
 *
 */
public class CreateObjOutputBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput> {

    private CreateObjGlobRes _globResult;
    private ObjId _objId;
    private List<Param> _param;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>> augmentation = Collections.emptyMap();

    public CreateObjOutputBuilder() {
    }
    public CreateObjOutputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjOutput arg) {
        this._objId = arg.getObjId();
        this._param = arg.getParam();
    }

    public CreateObjOutputBuilder(CreateObjOutput base) {
        this._globResult = base.getGlobResult();
        this._objId = base.getObjId();
        this._param = base.getParam();
        if (base instanceof CreateObjOutputImpl) {
            CreateObjOutputImpl impl = (CreateObjOutputImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }

    /**
     *Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjOutput</li>
     * </ul>
     *
     * @param arg grouping object
     * @throws IllegalArgumentException if given argument is none of valid types
    */
    public void fieldsFrom(DataObject arg) {
        boolean isValidArg = false;
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjOutput) {
            this._objId = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjOutput)arg).getObjId();
            this._param = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjOutput)arg).getParam();
            isValidArg = true;
        }
        if (!isValidArg) {
            throw new IllegalArgumentException(
              "expected one of: [org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjOutput] \n" +
              "but was: " + arg
            );
        }
    }

    public CreateObjGlobRes getGlobResult() {
        return _globResult;
    }
    
    public ObjId getObjId() {
        return _objId;
    }
    
    public List<Param> getParam() {
        return _param;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public CreateObjOutputBuilder setGlobResult(final CreateObjGlobRes value) {
        this._globResult = value;
        return this;
    }
    
     
    public CreateObjOutputBuilder setObjId(final ObjId value) {
        this._objId = value;
        return this;
    }
    
     
    public CreateObjOutputBuilder setParam(final List<Param> value) {
        this._param = value;
        return this;
    }
    
    public CreateObjOutputBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public CreateObjOutputBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public CreateObjOutput build() {
        return new CreateObjOutputImpl(this);
    }

    private static final class CreateObjOutputImpl implements CreateObjOutput {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput.class;
        }

        private final CreateObjGlobRes _globResult;
        private final ObjId _objId;
        private final List<Param> _param;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>> augmentation = Collections.emptyMap();

        private CreateObjOutputImpl(CreateObjOutputBuilder base) {
            this._globResult = base.getGlobResult();
            this._objId = base.getObjId();
            this._param = base.getParam();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
        }

        @Override
        public CreateObjGlobRes getGlobResult() {
            return _globResult;
        }
        
        @Override
        public ObjId getObjId() {
            return _objId;
        }
        
        @Override
        public List<Param> getParam() {
            return _param;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            result = prime * result + Objects.hashCode(_globResult);
            result = prime * result + Objects.hashCode(_objId);
            result = prime * result + Objects.hashCode(_param);
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput)obj;
            if (!Objects.equals(_globResult, other.getGlobResult())) {
                return false;
            }
            if (!Objects.equals(_objId, other.getObjId())) {
                return false;
            }
            if (!Objects.equals(_param, other.getParam())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                CreateObjOutputImpl otherImpl = (CreateObjOutputImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>> e : augmentation.entrySet()) {
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
            java.lang.String name = "CreateObjOutput [";
            java.lang.StringBuilder builder = new java.lang.StringBuilder (name);
            if (_globResult != null) {
                builder.append("_globResult=");
                builder.append(_globResult);
                builder.append(", ");
            }
            if (_objId != null) {
                builder.append("_objId=");
                builder.append(_objId);
                builder.append(", ");
            }
            if (_param != null) {
                builder.append("_param=");
                builder.append(_param);
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
