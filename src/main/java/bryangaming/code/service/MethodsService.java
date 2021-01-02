package bryangaming.code.service;

import bryangaming.code.methods.RankupMethod;
import bryangaming.code.methods.ScoreboardMethod;
import bryangaming.code.methods.SenderManager;
import bryangaming.code.methods.commands.CreateMethod;
import bryangaming.code.methods.commands.DeleteMethod;
import bryangaming.code.methods.commands.JoinMethod;
import bryangaming.code.methods.commands.QuitMethod;
import bryangaming.code.methods.events.BroadcastMethod;
import bryangaming.code.methods.events.ForceJoinMethod;
import bryangaming.code.methods.mode.EditMode;

public class MethodsService {

    private final PluginService pluginService;

    private SenderManager senderManager;
    private ScoreboardMethod scoreboardMethod;
    private RankupMethod rankupMethod;

    private ForceJoinMethod forceJoinMethod;
    private BroadcastMethod broadcastMethod;

    private CreateMethod createMethod;
    private DeleteMethod deleteMethod;

    private JoinMethod joinMethod;
    private QuitMethod quitMethod;

    private EditMode editMode;

    public MethodsService(PluginService pluginService){
        this.pluginService = pluginService;
        setup();
    }

    public void setup() {
        senderManager = new SenderManager(pluginService);
        scoreboardMethod = new ScoreboardMethod(pluginService);
        rankupMethod = new RankupMethod(pluginService);

        forceJoinMethod = new ForceJoinMethod(pluginService);
        broadcastMethod = new BroadcastMethod(pluginService);

        createMethod = new CreateMethod(pluginService);
        deleteMethod = new DeleteMethod(pluginService);

        joinMethod = new JoinMethod(pluginService);
        quitMethod = new QuitMethod(pluginService);

        editMode = new EditMode(pluginService);
    }

    public ForceJoinMethod getForceJoinMethod(){
        return forceJoinMethod;
    }

    public BroadcastMethod getBroadcastMethod(){
        return broadcastMethod;
    }

    public RankupMethod getRankupMethod() {
        return rankupMethod;
    }

    public CreateMethod getCreateMethod() {
        return createMethod;
    }

    public JoinMethod getJoinMethod() {
        return joinMethod;
    }

    public QuitMethod getQuitMethod() {
        return quitMethod;
    }

    public DeleteMethod getDeleteMethod() {
        return deleteMethod;
    }

    public SenderManager getSenderManager() {
        return senderManager;
    }

    public ScoreboardMethod getScoreboardMethod() {
        return scoreboardMethod;
    }

    public EditMode getEditMode(){
        return editMode;
    }
}
