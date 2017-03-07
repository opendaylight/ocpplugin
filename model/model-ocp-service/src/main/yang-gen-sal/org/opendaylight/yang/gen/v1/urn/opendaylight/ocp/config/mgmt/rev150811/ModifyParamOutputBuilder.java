package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifyparamoutput.Param;
import java.util.Objects;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamGlobRes;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput
 *
 */
public class ModifyParamOutputBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput> {

    private ModifyParamGlobRes _globResult;
    private ObjId _objId;
    private List<Param> _param;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>> augmentation = Collections.emptyMap();

    public ModifyParamOutputBuilder() {
    }
    public ModifyParamOutputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamOutput arg) {
        this._objId = arg.getObjId();
        this._param = arg.getParam();
    }

    public ModifyParamOutputBuilder(ModifyParamOutput base) {
        this._globResult = base.getGlobResult();
        this._objId = base.getObjId();
        this._param = base.getParam();
        if (base instanceof ModifyParamOutputImpl) {
            ModifyParamOutputImpl impl = (ModifyParamOutputImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }

    /**
     *Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamOutput</li>
     * </ul>
     *
     * @param arg grouping object
     * @throws IllegalArgumentException if given argument is none of valid types
    */
    public void fieldsFrom(DataObject arg) {
        boolean isValidArg = false;
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamOutput) {
            this._objId = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamOutput)arg).getObjId();
            this._param = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamOutput)arg).getParam();
            isValidArg = true;
        }
        if (!isValidArg) {
            throw new IllegalArgumentException(
              "expected one of: [org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamOutput] \n" +
              "but was: " + arg
            );
        }
    }

    public ModifyParamGlobRes getGlobResult() {
        return _globResult;
    }
    
    public ObjId getObjId() {
        return _objId;
    }
    
    public List<Param> getParam() {
        return _param;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public ModifyParamOutputBuilder setGlobResult(final ModifyParamGlobRes value) {
        this._globResult = value;
        return this;
    }
    
     
    public ModifyParamOutputBuilder setObjId(final ObjId value) {
        this._objId = value;
        return this;
    }
    
     
    public ModifyParamOutputBuilder setParam(final List<Param> value) {
        this._param = value;
        return this;
    }
    
    public ModifyParamOutputBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public ModifyParamOutputBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public ModifyParamOutput build() {
        return new ModifyParamOutputImpl(this);
    }

    private static final class ModifyParamOutputImpl implements ModifyParamOutput {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput.class;
        }

        private final ModifyParamGlobRes _globResult;
        private final ObjId _objId;
        private final List<Param> _param;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>> augmentation = Collections.emptyMap();

        private ModifyParamOutputImpl(ModifyParamOutputBuilder base) {
            this._globResult = base.getGlobResult();
            this._objId = base.getObjId();
            this._param = base.getParam();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
        }

        @Override
        public ModifyParamGlobRes getGlobResult() {
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
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput)obj;
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
                ModifyParamOutputImpl otherImpl = (ModifyParamOutputImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput>> e : augmentation.entrySet()) {
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
            java.lang.String name = "ModifyParamOutput [";
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
