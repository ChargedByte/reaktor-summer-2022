import { Module, VuexModule, VuexMutation } from "nuxt-property-decorator";

@Module({ name: "modules/general", stateFactory: true, namespaced: true })
export default class General extends VuexModule {
  liveGameDrawer = true;

  @VuexMutation
  setLiveGameDrawer(value: boolean) {
    this.liveGameDrawer = value;
  }
}
